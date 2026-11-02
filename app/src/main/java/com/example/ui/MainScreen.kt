package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.EmergencyHeaderBanner
import com.example.ui.components.FullScreenSosDisplay
import com.example.ui.screens.OfflineMapScreen
import com.example.ui.screens.SosRescueScreen
import com.example.ui.screens.StatusSenderScreen
import com.example.ui.screens.SuppliesLogScreen
import com.example.ui.screens.TriageGuideScreen
import com.example.ui.theme.AmberEmergency
import com.example.ui.theme.DangerSosRed

data class NavTabItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun MainScreen(
    viewModel: FloodAidViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isBlackoutMode by viewModel.isBlackoutMode.collectAsState()
    val isFlashlightOn by viewModel.isFlashlightOn.collectAsState()
    val gpsLocation by viewModel.currentLocation.collectAsState()
    val batteryPercent by viewModel.batteryPercent.collectAsState()
    val fullScreenSosVisible by viewModel.fullScreenSosVisible.collectAsState()
    val isSirenActive by viewModel.isSirenActive.collectAsState()

    val navTabs = listOf(
        NavTabItem("SOS Alert", Icons.Filled.Warning, Icons.Filled.Warning, "nav_sos"),
        NavTabItem("Offline Map", Icons.Filled.Map, Icons.Filled.Map, "nav_map"),
        NavTabItem("Protocols & AI", Icons.Filled.MedicalServices, Icons.Filled.MedicalServices, "nav_triage"),
        NavTabItem("Supplies", Icons.Filled.Backpack, Icons.Filled.Backpack, "nav_supplies"),
        NavTabItem("Status", Icons.Filled.Send, Icons.Filled.Send, "nav_status")
    )

    BackHandler(enabled = fullScreenSosVisible || selectedTab != 0) {
        if (fullScreenSosVisible) {
            viewModel.showFullScreenSos(false)
        } else if (selectedTab != 0) {
            viewModel.selectTab(0)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                EmergencyHeaderBanner(
                    gpsLocation = gpsLocation,
                    batteryPercent = batteryPercent,
                    isFlashlightOn = isFlashlightOn,
                    isBlackoutMode = isBlackoutMode,
                    onToggleFlashlight = { viewModel.toggleFlashlight() },
                    onToggleBlackout = { viewModel.toggleBlackoutMode() },
                    onRefreshLocation = { viewModel.refreshGpsLocation() },
                    modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    tonalElevation = 8.dp,
                    modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                ) {
                    navTabs.forEachIndexed { index, item ->
                        val isSelected = selectedTab == index
                        val isSosTab = index == 0
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { viewModel.selectTab(index) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title,
                                    tint = if (isSelected && isSosTab) DangerSosRed else if (isSelected) AmberEmergency else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            label = {
                                Text(
                                    text = item.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                                    color = if (isSelected && isSosTab) DangerSosRed else if (isSelected) AmberEmergency else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = if (isSosTab) DangerSosRed.copy(alpha = 0.2f) else AmberEmergency.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier.testTag(item.tag)
                        )
                    }
                }
            },
            modifier = modifier.fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> SosRescueScreen(viewModel = viewModel)
                    1 -> OfflineMapScreen(viewModel = viewModel)
                    2 -> TriageGuideScreen(viewModel = viewModel)
                    3 -> SuppliesLogScreen(viewModel = viewModel)
                    4 -> StatusSenderScreen(viewModel = viewModel)
                }
            }
        }

        // Full Screen SOS Signboard overlay
        if (fullScreenSosVisible) {
            FullScreenSosDisplay(
                gpsLocation = gpsLocation,
                batteryPercent = batteryPercent,
                isSirenActive = isSirenActive,
                onToggleSiren = { viewModel.toggleSiren() },
                onDismiss = { viewModel.showFullScreenSos(false) }
            )
        }
    }
}
