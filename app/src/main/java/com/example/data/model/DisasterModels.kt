package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emergency_contacts")
data class EmergencyContact(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phone: String,
    val category: String, // e.g., "Official", "Family", "Volunteer Boat", "Medical"
    val notes: String = "",
    val isPrimarySos: Boolean = false
)

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val status: String, // "Safe", "Needs Help", "Evacuated", "Missing"
    val lastKnownLocation: String,
    val batteryPercent: Int = 100,
    val medicalNeeds: String = "None",
    val bloodType: String = "Unknown",
    val lastCheckInTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "supplies_checklist")
data class SupplyItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String, // "Water & Food", "First Aid", "Power & Light", "Documents", "Sanitation"
    val quantityDetail: String,
    val isChecked: Boolean = false,
    val isLifeCritical: Boolean = false
)

@Entity(tableName = "water_level_logs")
data class WaterLevelLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val depthInches: Float,
    val locationTag: String, // "Porch", "First Floor", "Driveway", "Living Room"
    val trend: String, // "Rising Rapidly", "Rising Slowly", "Stationary", "Receding"
    val notes: String = ""
)

@Entity(tableName = "shelters")
data class ShelterItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val elevationMeters: Int,
    val currentCapacity: String, // "Open (Spaces Available)", "Near Capacity", "Full"
    val acceptsPets: Boolean = true,
    val hasGeneratorPower: Boolean = true,
    val hasMedicalStation: Boolean = false,
    val boatAccessible: Boolean = false,
    val contactPhone: String = "",
    val notes: String = ""
)

@Entity(tableName = "offline_map_packs")
data class OfflineMapPack(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val regionName: String,
    val centerLat: Double,
    val centerLon: Double,
    val radiusMiles: Int,
    val sizeBytes: Long, // e.g. 14_800_000 (14.8 MB)
    val isDownloaded: Boolean = false,
    val downloadProgress: Float = 0f,
    val lastDownloadedTime: Long? = null,
    val description: String = "",
    val shelterCount: Int = 8,
    val features: String = "OSM Roads, 10m Elevation, Boat Ramps, Flood Gates",
    val isActiveRegion: Boolean = false
)

data class TriageGuideItem(
    val id: String,
    val category: String,
    val title: String,
    val urgencyLevel: UrgencyLevel,
    val iconName: String,
    val summary: String,
    val doSteps: List<String>,
    val doNotSteps: List<String>,
    val multiLangTranslations: Map<String, String> = emptyMap() // "es", "vi", "fr", "hi"
)

enum class UrgencyLevel {
    LIFE_THREATENING,
    CRITICAL,
    IMPORTANT_PREP,
    RECOVERY
}
