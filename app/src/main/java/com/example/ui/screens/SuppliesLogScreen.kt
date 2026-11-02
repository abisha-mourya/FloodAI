package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.SupplyItem
import com.example.data.model.WaterLevelLog
import com.example.ui.FloodAidViewModel
import com.example.ui.components.LargeDisasterButton
import com.example.ui.theme.AmberEmergency
import com.example.ui.theme.DangerSosRed
import com.example.ui.theme.RescueCyan
import com.example.ui.theme.SafeGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun SuppliesLogScreen(
    viewModel: FloodAidViewModel,
    modifier: Modifier = Modifier
) {
    val supplies by viewModel.supplies.collectAsState()
    val waterLogs by viewModel.waterLogs.collectAsState()

    var activeSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Go-Bag Kit", "Water Calculator", "Flood Depth Log")

    var showAddSupplyDialog by remember { mutableStateOf(false) }
    var showAddLogDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Tab Row
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = activeSubTab == index,
                    onClick = { activeSubTab = index },
                    text = {
                        Text(
                            text = title,
                            fontWeight = if (activeSubTab == index) FontWeight.Black else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        when (activeSubTab) {
            0 -> SuppliesChecklistTab(
                supplies = supplies,
                onToggle = { id, current -> viewModel.toggleSupply(id, current) },
                onAddClick = { showAddSupplyDialog = true },
                onDelete = { viewModel.deleteSupply(it) }
            )
            1 -> WaterRationCalculatorTab()
            2 -> FloodDepthLoggerTab(
                waterLogs = waterLogs,
                onAddLogClick = { showAddLogDialog = true },
                onDeleteLog = { viewModel.deleteWaterLog(it) }
            )
        }
    }

    // Dialog for adding supply item
    if (showAddSupplyDialog) {
        var title by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Tools & Safety") }
        var detail by remember { mutableStateOf("") }
        var isCritical by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddSupplyDialog = false },
            title = { Text("Add Survival Supply Item", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Item Name (e.g. Life Jackets, Waterproof Matches)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = detail,
                        onValueChange = { detail = it },
                        label = { Text("Quantity / Notes") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { isCritical = !isCritical }
                    ) {
                        Checkbox(checked = isCritical, onCheckedChange = { isCritical = it })
                        Text("Life Critical (Mark with Red Warning)")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.addSupply(
                                SupplyItem(
                                    title = title,
                                    category = category,
                                    quantityDetail = detail.ifBlank { "Standard emergency ration" },
                                    isLifeCritical = isCritical
                                )
                            )
                            showAddSupplyDialog = false
                        }
                    }
                ) {
                    Text("Add Item")
                }
            },
            dismissButton = {
                Button(onClick = { showAddSupplyDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Dialog for adding flood depth log
    if (showAddLogDialog) {
        var depthStr by remember { mutableStateOf("12") }
        var locationTag by remember { mutableStateOf("Porch Step") }
        var trend by remember { mutableStateOf("Rising Slowly") }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddLogDialog = false },
            title = { Text("Log Water Level & Rise Rate", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = depthStr,
                        onValueChange = { depthStr = it },
                        label = { Text("Water Depth (Inches)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = locationTag,
                        onValueChange = { locationTag = it },
                        label = { Text("Measurement Point (Porch, Curb, Room)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Current Trend:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    val trends = listOf("Rising Rapidly", "Rising Slowly", "Stationary", "Receding")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        trends.forEach { t ->
                            FilterChip(
                                selected = trend == t,
                                onClick = { trend = t },
                                label = { Text(t, fontSize = 10.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (e.g. Rain intensified)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val d = depthStr.toFloatOrNull() ?: 6f
                        viewModel.addWaterLog(d, locationTag, trend, notes)
                        showAddLogDialog = false
                    }
                ) {
                    Text("Save Log")
                }
            },
            dismissButton = {
                Button(onClick = { showAddLogDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SuppliesChecklistTab(
    supplies: List<SupplyItem>,
    onToggle: (Long, Boolean) -> Unit,
    onAddClick: () -> Unit,
    onDelete: (SupplyItem) -> Unit
) {
    val checkedCount = supplies.count { it.isChecked }
    val totalCount = supplies.size
    val progress = if (totalCount > 0) checkedCount / totalCount.toFloat() else 0f

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Go-Bag Hero Image & Progress
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Image(
                    painter = painterResource(id = R.drawable.img_emergency_prep_guide_1790867898430),
                    contentDescription = "Survival Go-Bag Checklist",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 7f)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "72-HOUR SURVIVAL READINESS",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$checkedCount / $totalCount Packed",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Black,
                            color = if (progress >= 0.8f) SafeGreen else AmberEmergency
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (progress >= 0.8f) SafeGreen else AmberEmergency,
                        trackColor = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }

        // Add item row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SURVIVAL SUPPLIES CHECKLIST",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Item", fontSize = 12.sp)
            }
        }

        // Supplies list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(supplies, key = { it.id }) { item ->
                SupplyRowCard(item = item, onToggle = { onToggle(item.id, item.isChecked) }, onDelete = { onDelete(item) })
            }
        }
    }
}

@Composable
fun SupplyRowCard(
    item: SupplyItem,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isChecked) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (item.isLifeCritical && !item.isChecked) 2.dp else 1.dp,
                color = if (item.isLifeCritical && !item.isChecked) DangerSosRed else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = onToggle)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isChecked,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = SafeGreen,
                    checkmarkColor = Color.Black
                )
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (item.isLifeCritical) {
                        Surface(
                            color = DangerSosRed,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "CRITICAL",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isChecked) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = item.quantityDetail,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun WaterRationCalculatorTab() {
    var peopleCount by remember { mutableIntStateOf(3) }
    var petCount by remember { mutableIntStateOf(1) }
    var currentGallons by remember { mutableStateOf("12") }

    val gallons = currentGallons.toFloatOrNull() ?: 0f
    val dailyRequirement = (peopleCount * 1.0f) + (petCount * 0.5f)
    val daysRemaining = if (dailyRequirement > 0) gallons / dailyRequirement else 0f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, RescueCyan, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null, tint = RescueCyan, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("DRINKING WATER DURATION CALCULATOR", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black)
                }
                Text(
                    text = "FEMA disaster standard: 1 gallon per person per day for drinking and basic sanitation, plus 0.5 gallon per pet.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // People selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("People in Household:", fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (peopleCount > 1) peopleCount-- }, shape = RoundedCornerShape(8.dp)) { Text("-") }
                        Text("$peopleCount", fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 14.dp), fontSize = 18.sp)
                        Button(onClick = { peopleCount++ }, shape = RoundedCornerShape(8.dp)) { Text("+") }
                    }
                }

                // Pets selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Pets (Dogs / Cats):", fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Button(onClick = { if (petCount > 0) petCount-- }, shape = RoundedCornerShape(8.dp)) { Text("-") }
                        Text("$petCount", fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 14.dp), fontSize = 18.sp)
                        Button(onClick = { petCount++ }, shape = RoundedCornerShape(8.dp)) { Text("+") }
                    }
                }

                OutlinedTextField(
                    value = currentGallons,
                    onValueChange = { currentGallons = it },
                    label = { Text("Total Clean Water on Hand (Gallons)") },
                    modifier = Modifier.fillMaxWidth()
                )

                // Results card
                Surface(
                    color = if (daysRemaining < 3f) DangerSosRed.copy(alpha = 0.2f) else SafeGreen.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "WATER WILL LAST APPROXIMATELY:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "%.1f DAYS".format(daysRemaining),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black,
                            color = if (daysRemaining < 3f) DangerSosRed else SafeGreen
                        )
                        Text(
                            text = "Consumes %.1f gallons/day. %s".format(
                                dailyRequirement,
                                if (daysRemaining < 3f) "⚠️ CRITICAL: Below 72h survival reserve! Purify floodwater immediately." else "✅ Safe 72-hour reserve met."
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FloodDepthLoggerTab(
    waterLogs: List<WaterLevelLog>,
    onAddLogClick: () -> Unit,
    onDeleteLog: (WaterLevelLog) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("MMM dd, h:mm a", Locale.getDefault()) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "FLOODWATER RISE RATE TRACKER",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.primary
            )
            Button(
                onClick = onAddLogClick,
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Log Reading", fontSize = 12.sp)
            }
        }

        if (waterLogs.isEmpty()) {
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No water depth readings logged yet.", fontWeight = FontWeight.Bold)
                    Text("Log depth at regular intervals to detect if water is accelerating.", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(waterLogs, key = { it.id }) { log ->
                    val isRisingFast = log.trend.contains("Rapidly", ignoreCase = true)
                    Card(
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (isRisingFast) DangerSosRed else MaterialTheme.colorScheme.outline, RoundedCornerShape(10.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "%.0f inches (%.0f cm)".format(log.depthInches, log.depthInches * 2.54f),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black,
                                        color = if (isRisingFast) DangerSosRed else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Surface(
                                        color = if (isRisingFast) DangerSosRed.copy(alpha = 0.2f) else SafeGreen.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = log.trend,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isRisingFast) DangerSosRed else SafeGreen,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Location: ${log.locationTag} • ${dateFormat.format(Date(log.timestamp))}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (log.notes.isNotEmpty()) {
                                    Text(
                                        text = log.notes,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                            IconButton(onClick = { onDeleteLog(log) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}
