package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.TurnLeft
import androidx.compose.material.icons.filled.TurnRight
import androidx.compose.material.icons.filled.TurnSlightLeft
import androidx.compose.material.icons.filled.TurnSlightRight
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ActiveRoute
import com.example.data.model.ManeuverType
import com.example.data.model.RoadType
import com.example.data.model.ShelterItem
import com.example.ui.FloodAidViewModel
import com.example.ui.ShelterFilter
import com.example.ui.theme.AmberEmergency
import com.example.ui.theme.DangerSosRed
import com.example.ui.theme.RescueCyan
import com.example.ui.theme.SafeGreen
import com.example.util.LocationProvider
import com.example.util.OfflineTileManager

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OfflineMapScreen(
    viewModel: FloodAidViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tileManager = viewModel.tileManager

    val shelters by viewModel.filteredShelters.collectAsState()
    val gpsLocation by viewModel.currentLocation.collectAsState()
    val activeFilter by viewModel.shelterFilter.collectAsState()
    val mapPackInfo by viewModel.mapPackInfo.collectAsState()
    val isDownloading by viewModel.isDownloadingMap.collectAsState()
    val downloadProgress by viewModel.downloadProgress.collectAsState()
    val roadSegments by viewModel.roadSegments.collectAsState()
    val activeRoute by viewModel.activeRoute.collectAsState()
    val droppedPin by viewModel.droppedPin.collectAsState()
    val isDayMode by viewModel.isGoogleMapsDayMode.collectAsState()
    val isCalculatingRoute by viewModel.isCalculatingRoute.collectAsState()

    var showSetLocationDialog by remember { mutableStateOf(false) }
    var showAddShelterDialog by remember { mutableStateOf(false) }
    var showDirectionsSheet by remember { mutableStateOf(false) }

    // Map Pan and Zoom State
    var scale by remember { mutableFloatStateOf(1.0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    val currentGps = gpsLocation
    val centerLat = currentGps?.latitude ?: 37.7749
    val centerLon = currentGps?.longitude ?: -122.4194

    // Handle back button when navigating
    BackHandler(enabled = activeRoute != null || droppedPin != null) {
        if (activeRoute != null) {
            viewModel.stopNavigation()
        } else if (droppedPin != null) {
            viewModel.clearDroppedPin()
        }
    }

    // Colors
    val mapBackground = if (isDayMode) Color(0xFFF2F4F7) else Color(0xFF21252B)
    val highwayFill = if (isDayMode) Color(0xFFFFD54F) else Color(0xFFE69138)
    val highwayCasing = if (isDayMode) Color(0xFFFFB300) else Color(0xFFB45F06)
    val primaryRoadFill = if (isDayMode) Color(0xFFFFFFFF) else Color(0xFF383E47)
    val primaryRoadCasing = if (isDayMode) Color(0xFFCFD8DC) else Color(0xFF2D3239)
    val residentialRoadFill = if (isDayMode) Color(0xFFFFFFFF) else Color(0xFF2E343D)
    val waterFill = if (isDayMode) Color(0xFFAECBFA) else Color(0xFF1E3A5F)
    val parkFill = if (isDayMode) Color(0xFFCEEAD6) else Color(0xFF1B382B)
    val googleBlueRoute = Color(0xFF1A73E8)

    val zoom = 14

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // TOP GOOGLE MAPS FLOATING BAR
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = if (activeRoute != null) "Navigating to ${activeRoute!!.destinationName}" else "Real Offline Road Map (5 km)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Lat: %.4f, Lon: %.4f • Tap to drop pin".format(centerLat, centerLon),
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Day/Night Map Toggle
                    IconButton(
                        onClick = { viewModel.toggleMapTheme() },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isDayMode) Icons.Default.Nightlight else Icons.Default.WbSunny,
                            contentDescription = "Map Theme",
                            tint = if (isDayMode) Color(0xFF5F6368) else AmberEmergency,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Set coordinates pin
                    IconButton(
                        onClick = { showSetLocationDialog = true },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EditLocation,
                            contentDescription = "Set Location",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Download 5km button
                    Button(
                        onClick = { viewModel.download5KmAreaMap() },
                        enabled = !isDownloading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.height(32.dp)
                    ) {
                        if (isDownloading) {
                            CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = Color.White)
                        } else {
                            Icon(Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Download 5km", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        if (isDownloading || isCalculatingRoute) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 2.dp)) {
                if (isCalculatingRoute) {
                    Text(
                        text = "Snapping route to actual street network...",
                        style = MaterialTheme.typography.labelSmall,
                        color = googleBlueRoute,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = googleBlueRoute
                )
            }
        }

        // TURN-BY-TURN ACTIVE NAVIGATION HUD (Top banner when navigating)
        AnimatedVisibility(
            visible = activeRoute != null,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            if (activeRoute != null) {
                val route = activeRoute!!
                val currentStep = route.steps.getOrNull(route.currentStepIndex) ?: route.steps.first()

                Surface(
                    color = Color(0xFF0F9D58),
                    shape = RoundedCornerShape(14.dp),
                    tonalElevation = 8.dp,
                    shadowElevation = 6.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.25f)),
                                contentAlignment = Alignment.Center
                            ) {
                                val icon = when (currentStep.maneuver) {
                                    ManeuverType.TURN_LEFT -> Icons.Default.TurnLeft
                                    ManeuverType.TURN_RIGHT -> Icons.Default.TurnRight
                                    ManeuverType.SLIGHT_LEFT -> Icons.Default.TurnSlightLeft
                                    ManeuverType.SLIGHT_RIGHT -> Icons.Default.TurnSlightRight
                                    ManeuverType.ARRIVE -> Icons.Default.LocalHospital
                                    else -> Icons.Default.Navigation
                                }
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                if (currentStep.distanceMeters > 0) {
                                    Text(
                                        text = "In ${currentStep.distanceMeters} m",
                                        color = Color.White,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Text(
                                    text = currentStep.instruction,
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1
                                )
                                Text(
                                    text = "Step ${route.currentStepIndex + 1} of ${route.steps.size} • ${route.destinationName}",
                                    color = Color.White.copy(alpha = 0.85f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (route.currentStepIndex < route.steps.lastIndex) {
                                IconButton(
                                    onClick = { viewModel.nextNavigationStep() },
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color.White.copy(alpha = 0.3f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Next Step",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                            }
                            IconButton(
                                onClick = { viewModel.stopNavigation() },
                                modifier = Modifier
                                    .size(38.dp)
                                    .background(Color.Black.copy(alpha = 0.35f), CircleShape)
                            ) {
                                Icon(Icons.Default.Cancel, contentDescription = "Exit Navigation", tint = Color.White)
                            }
                        }
                    }
                }
            }
        }

        // REAL ROAD MAP CANVAS WITH LIVE & CACHED TILES
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(mapBackground)
                .border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(18.dp))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoomFactor, _ ->
                        scale = (scale * zoomFactor).coerceIn(0.6f, 4.5f)
                        offsetX += pan.x
                        offsetY += pan.y
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures { tapOffset ->
                        val cx = size.width / 2f + offsetX
                        val cy = size.height / 2f + offsetY

                        val centerTileX = OfflineTileManager.lonToTileX(centerLon, zoom)
                        val centerTileY = OfflineTileManager.latToTileY(centerLat, zoom)

                        val dx = tapOffset.x - cx
                        val dy = tapOffset.y - cy

                        val targetTileX = centerTileX + (dx / (256.0 * scale))
                        val targetTileY = centerTileY + (dy / (256.0 * scale))

                        val tappedLon = OfflineTileManager.tileXToLon(targetTileX, zoom)
                        val tappedLat = OfflineTileManager.tileYToLat(targetTileY, zoom)

                        viewModel.setDroppedPin(tappedLat, tappedLon, "Pin on Road")
                    }
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val cx = size.width / 2f + offsetX
                val cy = size.height / 2f + offsetY

                val centerTileX = OfflineTileManager.lonToTileX(centerLon, zoom)
                val centerTileY = OfflineTileManager.latToTileY(centerLat, zoom)

                fun toCanvasCoord(lat: Double, lon: Double): Offset {
                    val tx = OfflineTileManager.lonToTileX(lon, zoom)
                    val ty = OfflineTileManager.latToTileY(lat, zoom)
                    val x = cx + ((tx - centerTileX) * 256.0 * scale).toFloat()
                    val y = cy + ((ty - centerTileY) * 256.0 * scale).toFloat()
                    return Offset(x, y)
                }

                // 1. DRAW REAL MAP TILES (CartoDB / OpenStreetMap 5 km Area)
                val tileBounds = OfflineTileManager.compute5KmBounds(centerLat, centerLon, zoom)
                val tileSize = (256.0 * scale).toFloat()

                var tilesDrawn = 0
                for (tx in tileBounds.minTileX..tileBounds.maxTileX) {
                    for (ty in tileBounds.minTileY..tileBounds.maxTileY) {
                        val tileBmp = tileManager.getTileBitmap(zoom, tx, ty, scope)
                        val left = cx + ((tx - centerTileX) * 256.0 * scale).toFloat()
                        val top = cy + ((ty - centerTileY) * 256.0 * scale).toFloat()

                        if (tileBmp != null) {
                            drawImage(
                                image = tileBmp,
                                srcOffset = IntOffset.Zero,
                                srcSize = IntSize(tileBmp.width, tileBmp.height),
                                dstOffset = IntOffset(left.toInt(), top.toInt()),
                                dstSize = IntSize((tileSize + 1.5f).toInt(), (tileSize + 1.5f).toInt())
                            )
                            tilesDrawn++
                        }
                    }
                }

                // If tiles are loading or not yet cached, render vector road fallback
                if (tilesDrawn < 2) {
                    // Draw parks
                    val parkCenter1 = toCanvasCoord(centerLat + 0.012, centerLon - 0.018)
                    drawCircle(parkFill, radius = 55f * scale, center = parkCenter1)

                    val parkCenter2 = toCanvasCoord(centerLat - 0.018, centerLon + 0.022)
                    drawCircle(parkFill, radius = 50f * scale, center = parkCenter2)

                    // Draw waterways
                    val river = roadSegments.firstOrNull { it.type == RoadType.WATERWAY }
                    if (river != null && river.coordinates.size >= 2) {
                        val path = Path().apply {
                            val first = toCanvasCoord(river.coordinates[0].lat, river.coordinates[0].lon)
                            moveTo(first.x, first.y)
                            for (i in 1 until river.coordinates.size) {
                                val pt = toCanvasCoord(river.coordinates[i].lat, river.coordinates[i].lon)
                                lineTo(pt.x, pt.y)
                            }
                        }
                        drawPath(path, waterFill, style = Stroke(width = 14f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }

                    // Draw roads casing and fill
                    roadSegments.filter { it.type != RoadType.WATERWAY }.forEach { road ->
                        if (road.coordinates.size >= 2) {
                            val path = Path().apply {
                                val first = toCanvasCoord(road.coordinates[0].lat, road.coordinates[0].lon)
                                moveTo(first.x, first.y)
                                for (i in 1 until road.coordinates.size) {
                                    val pt = toCanvasCoord(road.coordinates[i].lat, road.coordinates[i].lon)
                                    lineTo(pt.x, pt.y)
                                }
                            }
                            val width = if (road.type == RoadType.HIGHWAY) 9f * scale else 5f * scale
                            val color = if (road.type == RoadType.HIGHWAY) highwayFill else primaryRoadFill
                            drawPath(path, color, style = Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round))
                        }
                    }
                }

                // 2. DRAW 5 KM EMERGENCY RADIUS BOUNDARY (Calculated to pixel accuracy)
                // 5 km in latitude offset is ~0.045 degrees
                val edgePoint5Km = toCanvasCoord(centerLat + 0.045, centerLon)
                val radius5KmPixels = kotlin.math.abs(edgePoint5Km.y - cy)

                drawCircle(
                    color = Color(0x1800B0FF),
                    radius = radius5KmPixels,
                    center = Offset(cx, cy)
                )
                drawCircle(
                    color = Color(0xFF00B0FF),
                    radius = radius5KmPixels,
                    center = Offset(cx, cy),
                    style = Stroke(
                        width = 3f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(14f, 10f), 0f)
                    )
                )

                // Concentric 2.5 km & 1.0 km rings
                drawCircle(
                    color = Color(0x44455A64),
                    radius = radius5KmPixels * 0.5f,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1f)
                )
                drawCircle(
                    color = Color(0x44455A64),
                    radius = radius5KmPixels * 0.2f,
                    center = Offset(cx, cy),
                    style = Stroke(width = 1f)
                )

                // 3. DRAW ACTIVE NAVIGATION ROUTE POLYLINE (Google Maps Blue #1A73E8)
                if (activeRoute != null && activeRoute!!.waypoints.size >= 2) {
                    val routePath = Path().apply {
                        val start = toCanvasCoord(activeRoute!!.waypoints[0].lat, activeRoute!!.waypoints[0].lon)
                        moveTo(start.x, start.y)
                        for (i in 1 until activeRoute!!.waypoints.size) {
                            val pt = toCanvasCoord(activeRoute!!.waypoints[i].lat, activeRoute!!.waypoints[i].lon)
                            lineTo(pt.x, pt.y)
                        }
                    }
                    drawPath(
                        routePath,
                        Color.White,
                        style = Stroke(width = 11f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )
                    drawPath(
                        routePath,
                        googleBlueRoute,
                        style = Stroke(width = 7.5f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
                    )

                    // Draw destination endpoint marker (clean Google Maps red pin, no dots on route)
                    val destPt = toCanvasCoord(activeRoute!!.destinationLat, activeRoute!!.destinationLon)
                    drawCircle(Color.Black.copy(alpha = 0.35f), radius = 10f * scale, center = Offset(destPt.x + 1f, destPt.y + 2f))
                    drawCircle(Color(0xFFEA4335), radius = 8f * scale, center = destPt)
                    drawCircle(Color.White, radius = 3.5f * scale, center = destPt)
                }

                // 4. DRAW HOSPITAL & SHELTER PINS
                shelters.forEach { shelter ->
                    val pos = toCanvasCoord(shelter.latitude, shelter.longitude)
                    val isHospital = shelter.hasMedicalStation || shelter.name.contains("Hospital", ignoreCase = true)
                    val pinColor = if (isHospital) DangerSosRed else SafeGreen

                    drawCircle(Color.Black.copy(alpha = 0.35f), radius = 12f * scale, center = Offset(pos.x + 2f, pos.y + 2f))
                    drawCircle(pinColor, radius = 10f * scale, center = pos)
                    drawCircle(Color.White, radius = 5.5f * scale, center = pos)
                    drawCircle(pinColor, radius = 2.5f * scale, center = pos)
                }

                // 5. DRAW DROPPED PIN (when user tapped map)
                if (droppedPin != null) {
                    val pinPos = toCanvasCoord(droppedPin!!.latitude, droppedPin!!.longitude)
                    drawCircle(Color.Black.copy(alpha = 0.45f), radius = 13f * scale, center = Offset(pinPos.x + 2f, pinPos.y + 4f))
                    drawCircle(Color(0xFFD93025), radius = 12f * scale, center = pinPos)
                    drawCircle(Color.White, radius = 6f * scale, center = pinPos)
                    drawCircle(Color(0xFFD93025), radius = 3f * scale, center = pinPos)
                }

                // 6. DRAW USER LIVE GPS LOCATION (Google Maps Blue Dot)
                val userPos = Offset(cx, cy)
                drawCircle(Color(0x331A73E8), radius = 22f * scale, center = userPos)
                drawCircle(Color.White, radius = 10f * scale, center = userPos)
                drawCircle(googleBlueRoute, radius = 7.5f * scale, center = userPos)
            }

            // FLOATING MAP ZOOM & RECENTER CONTROLS
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .shadow(2.dp, RoundedCornerShape(8.dp))
                        .clickable { scale = (scale + 0.35f).coerceAtMost(4.5f) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .shadow(2.dp, RoundedCornerShape(8.dp))
                        .clickable { scale = (scale - 0.35f).coerceAtLeast(0.6f) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .shadow(2.dp, RoundedCornerShape(8.dp))
                        .clickable {
                            scale = 1.0f
                            offsetX = 0f
                            offsetY = 0f
                            viewModel.refreshGpsLocation()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationSearching,
                        contentDescription = "My Location",
                        tint = googleBlueRoute,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // 5 KM RADIUS LEGEND
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black.copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(googleBlueRoute))
                Text("You", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("•", color = Color.Gray, fontSize = 10.sp)
                Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(DangerSosRed))
                Text("Hospital (<=5km)", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text("•", color = Color.Gray, fontSize = 10.sp)
                Text("Tap map to drop pin", color = AmberEmergency, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }

        // DROPPED PIN ACTION CARD (Appears when user taps map)
        if (droppedPin != null) {
            val pin = droppedPin!!
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, DangerSosRed),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DangerSosRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AddLocation, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text("DROPPED PIN ON ROAD", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DangerSosRed)
                            Text(
                                text = "%.4f° N, %.4f° W (%.2f km away)".format(pin.latitude, -pin.longitude, pin.distanceKm),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = {
                                viewModel.startNavigationTo(pin.latitude, pin.longitude, "Dropped Pin")
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = googleBlueRoute),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Icon(Icons.Default.Directions, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("NAVIGATE", fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }

                        IconButton(
                            onClick = { viewModel.clearDroppedPin() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Cancel, contentDescription = "Clear", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }
        }

        // NAVIGATION ACTIVE BOTTOM SUMMARY BAR
        if (activeRoute != null) {
            val route = activeRoute!!
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, googleBlueRoute),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${route.estimatedDrivingMinutes} min",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = SafeGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(%.2f km)".format(route.totalDistanceKm),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "${route.estimatedWalkingMinutes} min walk • Turn-by-Turn Offline Active",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { showDirectionsSheet = true },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Steps (${route.steps.size})", color = MaterialTheme.colorScheme.onSurface, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.stopNavigation() },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = DangerSosRed),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("END", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }

        // LIST OF SAVED REFUGE PINS & NAVIGATION TARGETS
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SAVED SAFE ZONES & PINS (${shelters.size})",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Button(
                onClick = { showAddShelterDialog = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.height(30.dp)
            ) {
                Icon(Icons.Default.AddLocation, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Custom Pin", fontSize = 10.sp)
            }
        }

        if (shelters.isEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AddLocation,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = "No custom refuge pins yet. Tap anywhere on the map or click 'Add Custom Pin' to save safe spots.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(shelters, key = { it.id }) { shelter ->
                val distKm = LocationProvider.calculateDistanceKm(centerLat, centerLon, shelter.latitude, shelter.longitude)
                val bearing = LocationProvider.calculateBearing(centerLat, centerLon, shelter.latitude, shelter.longitude)
                val cardinal = LocationProvider.bearingToCardinal(bearing)

                GoogleMapsHospitalCard(
                    shelter = shelter,
                    distanceKm = distKm,
                    cardinalDirection = cardinal,
                    onStartOfflineNavigation = {
                        viewModel.startNavigationTo(shelter.latitude, shelter.longitude, shelter.name)
                    },
                    onCall = {
                        if (shelter.contactPhone.isNotEmpty()) {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${shelter.contactPhone}"))
                            try { context.startActivity(intent) } catch (_: Exception) {}
                        }
                    }
                )
            }
        }
    }

    // Turn-by-Turn Steps Sheet Dialog
    if (showDirectionsSheet && activeRoute != null) {
        val route = activeRoute!!
        AlertDialog(
            onDismissRequest = { showDirectionsSheet = false },
            title = { Text("Route Steps to ${route.destinationName}", fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(route.steps) { step ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = when (step.maneuver) {
                                    ManeuverType.TURN_LEFT -> Icons.Default.TurnLeft
                                    ManeuverType.TURN_RIGHT -> Icons.Default.TurnRight
                                    ManeuverType.ARRIVE -> Icons.Default.LocalHospital
                                    else -> Icons.Default.Navigation
                                },
                                contentDescription = null,
                                tint = googleBlueRoute,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(step.instruction, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                if (step.distanceMeters > 0) {
                                    Text("${step.distanceMeters} m", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showDirectionsSheet = false }) { Text("Close") }
            }
        )
    }

    // Set Custom Location Dialog (with instant city presets)
    if (showSetLocationDialog) {
        var inputLat by remember { mutableStateOf("%.4f".format(centerLat)) }
        var inputLon by remember { mutableStateOf("%.4f".format(centerLon)) }

        val presets = listOf(
            Triple("My Live GPS", currentGps?.latitude ?: 37.7749, currentGps?.longitude ?: -122.4194),
            Triple("New Delhi", 28.6139, 77.2090),
            Triple("Mumbai", 19.0760, 72.8777),
            Triple("San Francisco", 37.7749, -122.4194),
            Triple("Miami", 25.7617, -80.1918),
            Triple("London", 51.5074, -0.1278)
        )

        AlertDialog(
            onDismissRequest = { showSetLocationDialog = false },
            title = { Text("Select / Enter Location Area", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Set your location. Real road map tiles and hospitals within a 5 km radius will download and display for that area.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text("Quick Presets:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        presets.forEach { (name, pLat, pLon) ->
                            FilterChip(
                                selected = false,
                                onClick = {
                                    inputLat = "%.4f".format(pLat)
                                    inputLon = "%.4f".format(pLon)
                                },
                                label = { Text(name, fontSize = 10.sp) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = inputLat,
                        onValueChange = { inputLat = it },
                        label = { Text("Latitude") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = inputLon,
                        onValueChange = { inputLon = it },
                        label = { Text("Longitude") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val lat = inputLat.toDoubleOrNull() ?: centerLat
                        val lon = inputLon.toDoubleOrNull() ?: centerLon
                        viewModel.setManualLocation(lat, lon, "User Specified Area")
                        showSetLocationDialog = false
                    }
                ) {
                    Text("Apply & Sync Road Map")
                }
            },
            dismissButton = {
                Button(onClick = { showSetLocationDialog = false }) { Text("Cancel") }
            }
        )
    }

    // Add Custom Safe Pin Dialog
    if (showAddShelterDialog) {
        var newName by remember { mutableStateOf("") }
        var newAddress by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddShelterDialog = false },
            title = { Text("Add Facility Pin in My 5 km Area", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        label = { Text("Name (e.g. City Hospital, Refuge Ridge)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newAddress,
                        onValueChange = { newAddress = it },
                        label = { Text("Street / Details") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newName.isNotBlank()) {
                            viewModel.addShelter(
                                ShelterItem(
                                    name = newName,
                                    address = newAddress.ifBlank { "Within 5 km radius" },
                                    latitude = centerLat + 0.006,
                                    longitude = centerLon + 0.007,
                                    elevationMeters = 30,
                                    currentCapacity = "User Verified Safe Point",
                                    acceptsPets = true,
                                    hasGeneratorPower = true,
                                    hasMedicalStation = true
                                )
                            )
                            showAddShelterDialog = false
                        }
                    }
                ) {
                    Text("Save Pin")
                }
            },
            dismissButton = {
                Button(onClick = { showAddShelterDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun GoogleMapsHospitalCard(
    shelter: ShelterItem,
    distanceKm: Double,
    cardinalDirection: String,
    onStartOfflineNavigation: () -> Unit,
    onCall: () -> Unit
) {
    val isHospital = shelter.hasMedicalStation || shelter.name.contains("Hospital", ignoreCase = true) || shelter.name.contains("Clinic", ignoreCase = true)
    val accentColor = if (isHospital) DangerSosRed else SafeGreen
    val googleBlue = Color(0xFF1A73E8)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.2.dp, accentColor.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isHospital) Icons.Default.LocalHospital else Icons.Default.AddLocation,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = shelter.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "%.2f km • Heading %s • %s".format(distanceKm, cardinalDirection, shelter.currentCapacity),
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (shelter.contactPhone.isNotEmpty()) {
                    IconButton(onClick = onCall, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.Call, contentDescription = "Call", tint = AmberEmergency, modifier = Modifier.size(18.dp))
                    }
                }

                Button(
                    onClick = onStartOfflineNavigation,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = googleBlue),
                    modifier = Modifier.height(34.dp)
                ) {
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ROUTE", fontSize = 11.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
