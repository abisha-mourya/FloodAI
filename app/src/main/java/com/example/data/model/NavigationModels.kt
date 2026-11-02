package com.example.data.model

data class OffsetCoord(
    val lat: Double,
    val lon: Double
)

enum class RoadType {
    HIGHWAY,
    PRIMARY,
    SECONDARY,
    RESIDENTIAL,
    WATERWAY
}

data class RoadSegment(
    val id: String,
    val name: String,
    val type: RoadType,
    val coordinates: List<OffsetCoord>
)

enum class ManeuverType {
    START,
    STRAIGHT,
    TURN_LEFT,
    TURN_RIGHT,
    SLIGHT_LEFT,
    SLIGHT_RIGHT,
    ARRIVE
}

data class NavigationStep(
    val instruction: String,
    val streetName: String,
    val distanceMeters: Int,
    val maneuver: ManeuverType,
    val stepLat: Double,
    val stepLon: Double
)

data class ActiveRoute(
    val destinationName: String,
    val destinationLat: Double,
    val destinationLon: Double,
    val totalDistanceKm: Double,
    val estimatedWalkingMinutes: Int,
    val estimatedDrivingMinutes: Int,
    val waypoints: List<OffsetCoord>,
    val steps: List<NavigationStep>,
    val currentStepIndex: Int = 0
)

data class DroppedPin(
    val latitude: Double,
    val longitude: Double,
    val label: String = "Dropped Pin",
    val distanceKm: Double = 0.0
)
