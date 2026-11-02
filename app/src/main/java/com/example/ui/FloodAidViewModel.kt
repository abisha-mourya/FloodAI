package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ActiveRoute
import com.example.data.model.DroppedPin
import com.example.data.model.EmergencyContact
import com.example.data.model.FamilyMember
import com.example.data.model.RoadSegment
import com.example.data.model.ShelterItem
import com.example.data.model.SupplyItem
import com.example.data.model.TriageGuideItem
import com.example.data.model.WaterLevelLog
import com.example.data.repository.FloodAidRepository
import com.example.util.GpsLocation
import com.example.util.LocationProvider
import com.example.util.NearbyHospitalService
import com.example.util.OfflineMapPackInfo
import com.example.util.OfflineRoutingEngine
import com.example.util.OfflineTileManager
import com.example.util.OfflineTriageEngine
import com.example.util.SosBeaconController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ShelterFilter {
    ALL,
    PETS_WELCOME,
    BOAT_ACCESSIBLE,
    MEDICAL_STATION,
    HIGH_ELEVATION
}

class FloodAidViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application, viewModelScope)
    private val repository = FloodAidRepository(database)

    val sosController = SosBeaconController(application)
    val locationProvider = LocationProvider(application)
    val hospitalService = NearbyHospitalService(application)
    val tileManager = OfflineTileManager(application)

    // Hardware StateFlows
    val isFlashlightOn: StateFlow<Boolean> = sosController.isFlashlightOn
    val isStrobeActive: StateFlow<Boolean> = sosController.isStrobeActive
    val isSirenActive: StateFlow<Boolean> = sosController.isSirenActive
    val currentLocation: StateFlow<GpsLocation?> = locationProvider.currentLocation

    val mapPackInfo: StateFlow<OfflineMapPackInfo> = hospitalService.mapPackInfo
    val isDownloadingMap: StateFlow<Boolean> = hospitalService.isDownloading
    val downloadProgress: StateFlow<Float> = hospitalService.downloadProgress

    // Navigation and Map StateFlows
    private val _activeRoute = MutableStateFlow<ActiveRoute?>(null)
    val activeRoute: StateFlow<ActiveRoute?> = _activeRoute.asStateFlow()

    private val _droppedPin = MutableStateFlow<DroppedPin?>(null)
    val droppedPin: StateFlow<DroppedPin?> = _droppedPin.asStateFlow()

    private val _roadSegments = MutableStateFlow<List<RoadSegment>>(emptyList())
    val roadSegments: StateFlow<List<RoadSegment>> = _roadSegments.asStateFlow()

    private val _isGoogleMapsDayMode = MutableStateFlow(true)
    val isGoogleMapsDayMode: StateFlow<Boolean> = _isGoogleMapsDayMode.asStateFlow()

    private val _isCalculatingRoute = MutableStateFlow(false)
    val isCalculatingRoute: StateFlow<Boolean> = _isCalculatingRoute.asStateFlow()

    private var hasAutoSynced = false

    private val _batteryPercent = MutableStateFlow(locationProvider.getDeviceBatteryPercent())
    val batteryPercent: StateFlow<Int> = _batteryPercent.asStateFlow()

    private val _isBlackoutMode = MutableStateFlow(false)
    val isBlackoutMode: StateFlow<Boolean> = _isBlackoutMode.asStateFlow()

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("en")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _triageSearchQuery = MutableStateFlow("")
    val triageSearchQuery: StateFlow<String> = _triageSearchQuery.asStateFlow()

    private val _fullScreenSosVisible = MutableStateFlow(false)
    val fullScreenSosVisible: StateFlow<Boolean> = _fullScreenSosVisible.asStateFlow()

    private val _shelterFilter = MutableStateFlow(ShelterFilter.ALL)
    val shelterFilter: StateFlow<ShelterFilter> = _shelterFilter.asStateFlow()

    // Room Database Flows
    val contacts: StateFlow<List<EmergencyContact>> = repository.allContacts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val familyMembers: StateFlow<List<FamilyMember>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val supplies: StateFlow<List<SupplyItem>> = repository.allSupplies
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val waterLogs: StateFlow<List<WaterLevelLog>> = repository.allWaterLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allShelters: StateFlow<List<ShelterItem>> = repository.allShelters
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Shelters list - contains ONLY user-added custom pins (no default mock pins)
    val filteredShelters: StateFlow<List<ShelterItem>> = allShelters

    // Triage guides
    val triageGuides: StateFlow<List<TriageGuideItem>> = combine(_triageSearchQuery) { (query) ->
        OfflineTriageEngine.searchGuides(query)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), OfflineTriageEngine.allGuides)

    init {
        refreshGpsLocation()
        refreshBattery()

        // Purge any default mock pins from database so map starts clean with only user pins
        viewModelScope.launch {
            repository.clearAllShelters()
        }

        // Generate local road network on start or location change
        viewModelScope.launch {
            currentLocation.collect { loc ->
                if (loc != null) {
                    _roadSegments.value = OfflineRoutingEngine.generateLocalRoadNetwork(loc.latitude, loc.longitude)
                    if (!hasAutoSynced) {
                        hasAutoSynced = true
                        download5KmAreaMap(loc.latitude, loc.longitude)
                    }
                }
            }
        }
    }

    fun download5KmAreaMap(lat: Double? = null, lon: Double? = null) {
        viewModelScope.launch {
            val targetLat = lat ?: currentLocation.value?.latitude ?: 37.7749
            val targetLon = lon ?: currentLocation.value?.longitude ?: -122.4194
            _roadSegments.value = OfflineRoutingEngine.generateLocalRoadNetwork(targetLat, targetLon)
            tileManager.download5KmRadiusTiles(targetLat, targetLon, 14)
        }
    }

    fun clearAndRedownloadMap() {
        tileManager.clearCache()
        download5KmAreaMap()
    }

    fun setManualLocation(lat: Double, lon: Double, label: String = "Selected Coordinates") {
        locationProvider.setManualLocation(lat, lon, label)
        _roadSegments.value = OfflineRoutingEngine.generateLocalRoadNetwork(lat, lon)
        download5KmAreaMap(lat, lon)
    }

    // Navigation methods
    fun startNavigationTo(destLat: Double, destLon: Double, destName: String) {
        viewModelScope.launch {
            _isCalculatingRoute.value = true
            val startLat = currentLocation.value?.latitude ?: 37.7749
            val startLon = currentLocation.value?.longitude ?: -122.4194
            val route = OfflineRoutingEngine.calculateRoute(
                startLat = startLat,
                startLon = startLon,
                destLat = destLat,
                destLon = destLon,
                destName = destName,
                cacheDir = getApplication<Application>().filesDir
            )
            _activeRoute.value = route
            _isCalculatingRoute.value = false
        }
    }

    fun stopNavigation() {
        _activeRoute.value = null
    }

    fun nextNavigationStep() {
        _activeRoute.value?.let { current ->
            if (current.currentStepIndex < current.steps.lastIndex) {
                _activeRoute.value = current.copy(currentStepIndex = current.currentStepIndex + 1)
            }
        }
    }

    fun setDroppedPin(lat: Double, lon: Double, label: String = "Dropped Pin") {
        val centerLat = currentLocation.value?.latitude ?: 37.7749
        val centerLon = currentLocation.value?.longitude ?: -122.4194
        val distKm = LocationProvider.calculateDistanceKm(centerLat, centerLon, lat, lon)
        _droppedPin.value = DroppedPin(
            latitude = lat,
            longitude = lon,
            label = label,
            distanceKm = distKm
        )
    }

    fun clearDroppedPin() {
        _droppedPin.value = null
    }

    fun toggleMapTheme() {
        _isGoogleMapsDayMode.value = !_isGoogleMapsDayMode.value
    }

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }

    fun toggleBlackoutMode() {
        _isBlackoutMode.value = !_isBlackoutMode.value
    }

    fun setLanguage(lang: String) {
        _selectedLanguage.value = lang
    }

    fun setTriageSearchQuery(query: String) {
        _triageSearchQuery.value = query
    }

    fun setShelterFilter(filter: ShelterFilter) {
        _shelterFilter.value = filter
    }

    fun showFullScreenSos(show: Boolean) {
        _fullScreenSosVisible.value = show
    }

    fun refreshGpsLocation() {
        locationProvider.requestLocationUpdate()
    }

    fun refreshBattery() {
        _batteryPercent.value = locationProvider.getDeviceBatteryPercent()
    }

    // Hardware Actions
    fun toggleStrobe() {
        sosController.toggleStrobe(viewModelScope)
    }

    fun toggleSiren() {
        sosController.toggleSiren(viewModelScope)
    }

    fun toggleFlashlight() {
        sosController.toggleFlashlight()
    }

    fun stopEmergencySignals() {
        sosController.stopAll()
        _fullScreenSosVisible.value = false
    }

    // Compose rescue SMS
    fun generateRescueSmsMessage(
        peopleCount: Int = 1,
        waterDepthDesc: String = "Rising to 1st floor",
        medicalUrgency: String = "None"
    ): String {
        val loc = currentLocation.value
        val coords = if (loc != null) {
            "%.5f, %.5f".format(loc.latitude, loc.longitude)
        } else {
            "Unknown GPS"
        }
        val batt = batteryPercent.value
        return "SOS RESCUE NEEDED! People: $peopleCount. Water: $waterDepthDesc. Medical: $medicalUrgency. GPS: $coords. Battery: $batt%. Please send boat/rescue crew!"
    }

    // Repository actions
    fun toggleSupply(id: Long, currentVal: Boolean) {
        viewModelScope.launch {
            repository.setSupplyChecked(id, !currentVal)
        }
    }

    fun addSupply(item: SupplyItem) {
        viewModelScope.launch {
            repository.insertSupply(item)
        }
    }

    fun deleteSupply(item: SupplyItem) {
        viewModelScope.launch {
            repository.deleteSupply(item)
        }
    }

    fun addWaterLog(depth: Float, location: String, trend: String, notes: String) {
        viewModelScope.launch {
            repository.insertWaterLog(
                WaterLevelLog(
                    depthInches = depth,
                    locationTag = location,
                    trend = trend,
                    notes = notes
                )
            )
        }
    }

    fun deleteWaterLog(log: WaterLevelLog) {
        viewModelScope.launch {
            repository.deleteWaterLog(log)
        }
    }

    fun addContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.insertContact(contact)
        }
    }

    fun deleteContact(contact: EmergencyContact) {
        viewModelScope.launch {
            repository.deleteContact(contact)
        }
    }

    fun addFamilyMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.insertMember(member)
        }
    }

    fun updateFamilyMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.updateMember(member)
        }
    }

    fun deleteFamilyMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.deleteMember(member)
        }
    }

    fun addShelter(shelter: ShelterItem) {
        viewModelScope.launch {
            repository.insertShelter(shelter)
        }
    }

    override fun onCleared() {
        super.onCleared()
        sosController.stopAll()
    }
}
