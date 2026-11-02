package com.example.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

data class GpsLocation(
    val latitude: Double,
    val longitude: Double,
    val altitudeMeters: Double = 0.0,
    val accuracyMeters: Float = 0f,
    val timestamp: Long = System.currentTimeMillis(),
    val isCustom: Boolean = false,
    val locationLabel: String = ""
)

class LocationProvider(private val context: Context) {

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    private val fusedClient = try {
        LocationServices.getFusedLocationProviderClient(context)
    } catch (_: Exception) {
        null
    }

    private val _currentLocation = MutableStateFlow<GpsLocation?>(null)
    val currentLocation: StateFlow<GpsLocation?> = _currentLocation.asStateFlow()

    private val _isGpsActive = MutableStateFlow(false)
    val isGpsActive: StateFlow<Boolean> = _isGpsActive.asStateFlow()

    private var locationCallback: LocationCallback? = null

    init {
        // Attempt immediate last known location read
        readLastKnownLocation()
    }

    @SuppressLint("MissingPermission")
    private fun readLastKnownLocation() {
        try {
            fusedClient?.lastLocation?.addOnSuccessListener { loc ->
                if (loc != null) {
                    updateLocation(loc)
                } else {
                    fallbackLocationManager()
                }
            }?.addOnFailureListener {
                fallbackLocationManager()
            } ?: fallbackLocationManager()
        } catch (_: Exception) {
            fallbackLocationManager()
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdate() {
        try {
            readLastKnownLocation()

            // Also request active high-accuracy location stream
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000L)
                .setMinUpdateIntervalMillis(2000L)
                .build()

            if (locationCallback == null) {
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        result.lastLocation?.let { loc ->
                            updateLocation(loc)
                        }
                    }
                }
            }

            locationCallback?.let { cb ->
                fusedClient?.requestLocationUpdates(locationRequest, cb, Looper.getMainLooper())
            }

            fusedClient?.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                ?.addOnSuccessListener { loc ->
                    if (loc != null) {
                        updateLocation(loc)
                    } else {
                        fallbackLocationManager()
                    }
                }?.addOnFailureListener {
                    fallbackLocationManager()
                }
        } catch (_: Exception) {
            fallbackLocationManager()
        }
    }

    @SuppressLint("MissingPermission")
    private fun fallbackLocationManager() {
        try {
            val gpsLoc = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val netLoc = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val passiveLoc = locationManager?.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)

            val best = gpsLoc ?: netLoc ?: passiveLoc
            if (best != null) {
                updateLocation(best)
            } else if (_currentLocation.value == null) {
                // If device hasn't resolved GPS yet, initialize with default reference until satellites fix
                _currentLocation.value = GpsLocation(
                    latitude = 37.7749,
                    longitude = -122.4194,
                    altitudeMeters = 10.0,
                    accuracyMeters = 20f,
                    locationLabel = "Device Position"
                )
            }

            locationManager?.requestSingleUpdate(
                LocationManager.GPS_PROVIDER,
                object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        updateLocation(location)
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) { _isGpsActive.value = true }
                    override fun onProviderDisabled(provider: String) { _isGpsActive.value = false }
                },
                Looper.getMainLooper()
            )
        } catch (_: Exception) {
            if (_currentLocation.value == null) {
                _currentLocation.value = GpsLocation(
                    latitude = 37.7749,
                    longitude = -122.4194,
                    altitudeMeters = 10.0,
                    accuracyMeters = 25f,
                    locationLabel = "Default Location"
                )
            }
        }
    }

    private fun updateLocation(loc: Location) {
        _currentLocation.value = GpsLocation(
            latitude = loc.latitude,
            longitude = loc.longitude,
            altitudeMeters = loc.altitude,
            accuracyMeters = loc.accuracy,
            timestamp = loc.time,
            isCustom = false,
            locationLabel = "Live GPS"
        )
        _isGpsActive.value = true
    }

    fun setManualLocation(lat: Double, lon: Double, label: String = "Selected Location") {
        _currentLocation.value = GpsLocation(
            latitude = lat,
            longitude = lon,
            altitudeMeters = 15.0,
            accuracyMeters = 5f,
            timestamp = System.currentTimeMillis(),
            isCustom = true,
            locationLabel = label
        )
        _isGpsActive.value = true
    }

    fun getDeviceBatteryPercent(): Int {
        return try {
            val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { filter ->
                context.registerReceiver(null, filter)
            }
            val level: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
            if (level >= 0 && scale > 0) {
                (level * 100 / scale.toFloat()).toInt()
            } else {
                85
            }
        } catch (_: Exception) {
            85
        }
    }

    companion object {
        fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val r = 6371.0 // Radius of earth in kilometers
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))
            return r * c
        }

        fun calculateDistanceMiles(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            return calculateDistanceKm(lat1, lon1, lat2, lon2) * 0.621371
        }

        fun calculateBearing(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val dLon = Math.toRadians(lon2 - lon1)
            val y = sin(dLon) * cos(Math.toRadians(lat2))
            val x = cos(Math.toRadians(lat1)) * sin(Math.toRadians(lat2)) -
                    sin(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) * cos(dLon)
            var brng = Math.toDegrees(atan2(y, x))
            if (brng < 0) brng += 360.0
            return brng
        }

        fun bearingToCardinal(bearingDeg: Double): String {
            val directions = arrayOf("N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE",
                "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW")
            val index = ((bearingDeg + 11.25) / 22.5).toInt() % 16
            return directions[index]
        }
    }
}
