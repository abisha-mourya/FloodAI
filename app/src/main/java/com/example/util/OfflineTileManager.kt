package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.collection.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.tan

data class MapTileBounds(
    val minTileX: Int,
    val maxTileX: Int,
    val minTileY: Int,
    val maxTileY: Int,
    val zoom: Int
)

class OfflineTileManager(private val context: Context) {

    private val tilesDir = File(context.filesDir, "offline_map_tiles").apply { mkdirs() }
    private val memoryCache = LruCache<String, ImageBitmap>(64)

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    private val _downloadedCount = MutableStateFlow(0)
    val downloadedCount: StateFlow<Int> = _downloadedCount.asStateFlow()

    private val _isDownloadingTiles = MutableStateFlow(false)
    val isDownloadingTiles: StateFlow<Boolean> = _isDownloadingTiles.asStateFlow()

    private val pendingFetches = mutableSetOf<String>()

    init {
        // Automatically purge any old watermarked Carto tiles
        val prefs = context.getSharedPreferences("offline_tiles_prefs", Context.MODE_PRIVATE)
        val version = prefs.getInt("tile_provider_version", 1)
        if (version < 3) {
            tilesDir.listFiles()?.forEach { it.delete() }
            memoryCache.evictAll()
            prefs.edit().putInt("tile_provider_version", 3).apply()
        }
        countCachedTiles()
    }

    fun countCachedTiles() {
        val files = tilesDir.listFiles()?.filter { it.extension == "png" || it.extension == "jpg" }
        _downloadedCount.value = files?.size ?: 0
    }

    fun clearCache() {
        tilesDir.listFiles()?.forEach { it.delete() }
        memoryCache.evictAll()
        countCachedTiles()
    }

    fun getTileBitmap(zoom: Int, tileX: Int, tileY: Int, scope: CoroutineScope): ImageBitmap? {
        val key = "${zoom}_${tileX}_${tileY}"
        val mem = memoryCache.get(key)
        if (mem != null) return mem

        // Check local disk cache (supports png or jpg)
        val filePng = File(tilesDir, "$key.png")
        val fileJpg = File(tilesDir, "$key.jpg")
        val targetFile = if (filePng.exists() && filePng.length() > 0) filePng else if (fileJpg.exists() && fileJpg.length() > 0) fileJpg else null

        if (targetFile != null) {
            try {
                val bmp = BitmapFactory.decodeFile(targetFile.absolutePath)
                if (bmp != null) {
                    val imgBmp = bmp.asImageBitmap()
                    memoryCache.put(key, imgBmp)
                    return imgBmp
                }
            } catch (_: Exception) {}
        }

        // If not cached, trigger background fetch if not already in progress
        if (!pendingFetches.contains(key)) {
            pendingFetches.add(key)
            scope.launch(Dispatchers.IO) {
                downloadSingleTile(zoom, tileX, tileY)
                pendingFetches.remove(key)
            }
        }

        return null
    }

    private fun downloadSingleTile(zoom: Int, tileX: Int, tileY: Int): Boolean {
        val key = "${zoom}_${tileX}_${tileY}"
        val filePng = File(tilesDir, "$key.png")
        val fileJpg = File(tilesDir, "$key.jpg")
        if ((filePng.exists() && filePng.length() > 0) || (fileJpg.exists() && fileJpg.length() > 0)) return true

        // Reliable 100% Free Public Tile Servers - NO API KEY REQUIRED, NO WATERMARK!
        // 1. Esri World Street Map (high resolution, Google Maps-style streets, highways, parks, labels)
        // 2. OpenStreetMap Standard (official worldwide crowdsourced map)
        // 3. OpenStreetMap France Regional
        val providers = listOf(
            "https://server.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer/tile/$zoom/$tileY/$tileX" to fileJpg,
            "https://tile.openstreetmap.org/$zoom/$tileX/$tileY.png" to filePng,
            "https://a.tile.openstreetmap.fr/osmfr/$zoom/$tileX/$tileY.png" to filePng
        )

        for ((url, destFile) in providers) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "FloodAid-DisasterRelief-App/1.0 (Android Emergency Offline; contact: relief@floodaid.org)")
                    .build()

                val response = httpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val bytes = response.body?.bytes()
                    if (bytes != null && bytes.size > 800) {
                        FileOutputStream(destFile).use { it.write(bytes) }
                        val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        if (bmp != null) {
                            memoryCache.put(key, bmp.asImageBitmap())
                        }
                        return true
                    }
                }
            } catch (_: Exception) {}
        }
        return false
    }

    suspend fun download5KmRadiusTiles(
        centerLat: Double,
        centerLon: Double,
        zoom: Int = 14
    ): Int = withContext(Dispatchers.IO) {
        _isDownloadingTiles.value = true

        val bounds = compute5KmBounds(centerLat, centerLon, zoom)
        var successCount = 0

        for (tx in bounds.minTileX..bounds.maxTileX) {
            for (ty in bounds.minTileY..bounds.maxTileY) {
                if (downloadSingleTile(zoom, tx, ty)) {
                    successCount++
                }
            }
        }

        countCachedTiles()
        _isDownloadingTiles.value = false
        successCount
    }

    companion object {
        fun compute5KmBounds(centerLat: Double, centerLon: Double, zoom: Int = 14): MapTileBounds {
            val deltaLat = 0.045
            val deltaLon = 0.045 / cos(Math.toRadians(centerLat)).coerceAtLeast(0.1)

            val minLat = centerLat - deltaLat
            val maxLat = centerLat + deltaLat
            val minLon = centerLon - deltaLon
            val maxLon = centerLon + deltaLon

            val minX = lonToTileX(minLon, zoom).toInt()
            val maxX = lonToTileX(maxLon, zoom).toInt()
            val minY = latToTileY(maxLat, zoom).toInt()
            val maxY = latToTileY(minLat, zoom).toInt()

            return MapTileBounds(
                minTileX = minX.coerceAtLeast(0),
                maxTileX = maxX.coerceAtMost((1 shl zoom) - 1),
                minTileY = minY.coerceAtLeast(0),
                maxTileY = maxY.coerceAtMost((1 shl zoom) - 1),
                zoom = zoom
            )
        }

        fun lonToTileX(lon: Double, zoom: Int): Double {
            return (lon + 180.0) / 360.0 * (1 shl zoom)
        }

        fun latToTileY(lat: Double, zoom: Int): Double {
            val latRad = Math.toRadians(lat.coerceIn(-85.0511, 85.0511))
            return (1.0 - ln(tan(latRad) + 1.0 / cos(latRad)) / Math.PI) / 2.0 * (1 shl zoom)
        }

        fun tileXToLon(x: Double, zoom: Int): Double {
            return x / (1 shl zoom) * 360.0 - 180.0
        }

        fun tileYToLat(y: Double, zoom: Int): Double {
            val n = Math.PI - 2.0 * Math.PI * y / (1 shl zoom)
            return Math.toDegrees(Math.atan(Math.sinh(n)))
        }
    }
}
