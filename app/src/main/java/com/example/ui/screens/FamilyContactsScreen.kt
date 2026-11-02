package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContact
import com.example.data.model.FamilyMember
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
fun FamilyContactsScreen(
    viewModel: FloodAidViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val familyMembers by viewModel.familyMembers.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val gpsLocation by viewModel.currentLocation.collectAsState()
    val deviceBattery by viewModel.batteryPercent.collectAsState()

    var activeSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Family Status (${familyMembers.size})", "Emergency Numbers (${contacts.size})")

    var showAddMemberDialog by remember { mutableStateOf(false) }
    var showAddContactDialog by remember { mutableStateOf(false) }
    var memberToEdit by remember { mutableStateOf<FamilyMember?>(null) }

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

        if (activeSubTab == 0) {
            // Family check-in ledger
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FAMILY SAFETY & MEDICAL STATUS",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = { showAddMemberDialog = true },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Member", fontSize = 12.sp)
                }
            }

            // Quick Status Broadcast Button
            LargeDisasterButton(
                text = "BROADCAST STATUS TO FAMILY (SMS)",
                subtext = "Sends GPS coordinates and safety update to all contacts",
                icon = Icons.Default.Share,
                backgroundColor = SafeGreen,
                contentColor = Color.Black,
                onClick = {
                    val currentGps = gpsLocation
                    val coords = if (currentGps != null) "%.5f, %.5f".format(currentGps.latitude, currentGps.longitude) else "Offline"
                    val msg = "FAMILY UPDATE: We are currently safe. Battery: $deviceBattery%. GPS: $coords. Network is limited, please do not worry. Will update again soon."
                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, msg)
                        type = "text/plain"
                    }
                    try {
                        context.startActivity(Intent.createChooser(sendIntent, "Share Status"))
                    } catch (_: Exception) {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("Family Status", msg))
                        Toast.makeText(context, "Copied status message to clipboard", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(familyMembers, key = { it.id }) { member ->
                    FamilyMemberCard(
                        member = member,
                        onEdit = { memberToEdit = member },
                        onDelete = { viewModel.deleteFamilyMember(member) }
                    )
                }
            }
        } else {
            // Emergency hotlines & personal contacts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OFFLINE EMERGENCY DIRECTORY",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Button(
                    onClick = { showAddContactDialog = true },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Contact", fontSize = 12.sp)
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(contacts, key = { it.id }) { contact ->
                    EmergencyContactCard(
                        contact = contact,
                        onCall = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${contact.phone}"))
                            try {
                                context.startActivity(intent)
                            } catch (_: Exception) {}
                        },
                        onDelete = { viewModel.deleteContact(contact) }
                    )
                }
            }
        }
    }

    // Add Family Member Dialog
    if (showAddMemberDialog) {
        var name by remember { mutableStateOf("") }
        var status by remember { mutableStateOf("Safe") }
        var location by remember { mutableStateOf("Home 2nd Floor") }
        var meds by remember { mutableStateOf("None") }
        var blood by remember { mutableStateOf("O+") }

        AlertDialog(
            onDismissRequest = { showAddMemberDialog = false },
            title = { Text("Add Family Member Check-In", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name (e.g. Dad, Sarah, Rover)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text("Safety Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    val statusOptions = listOf("Safe", "Needs Help", "Evacuated", "Missing")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        statusOptions.forEach { s ->
                            FilterChip(
                                selected = status == s,
                                onClick = { status = s },
                                label = { Text(s, fontSize = 11.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Current Location / Shelter") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = meds,
                        onValueChange = { meds = it },
                        label = { Text("Medical Needs (Insulin, Oxygen, Inhaler)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            viewModel.addFamilyMember(
                                FamilyMember(
                                    name = name,
                                    status = status,
                                    lastKnownLocation = location.ifBlank { "Unknown" },
                                    medicalNeeds = meds.ifBlank { "None" },
                                    bloodType = blood,
                                    lastCheckInTime = System.currentTimeMillis()
                                )
                            )
                            showAddMemberDialog = false
                        }
                    }
                ) {
                    Text("Save Member")
                }
            },
            dismissButton = {
                Button(onClick = { showAddMemberDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Edit Member Status Dialog
    if (memberToEdit != null) {
        val current = memberToEdit!!
        var status by remember { mutableStateOf(current.status) }
        var location by remember { mutableStateOf(current.lastKnownLocation) }
        var meds by remember { mutableStateOf(current.medicalNeeds) }

        AlertDialog(
            onDismissRequest = { memberToEdit = null },
            title = { Text("Update ${current.name}'s Status", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Status:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    val statusOptions = listOf("Safe", "Needs Help", "Evacuated", "Missing")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        statusOptions.forEach { s ->
                            FilterChip(
                                selected = status == s,
                                onClick = { status = s },
                                label = { Text(s, fontSize = 11.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
                        label = { Text("Location") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = meds,
                        onValueChange = { meds = it },
                        label = { Text("Medical Needs") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.updateFamilyMember(
                            current.copy(
                                status = status,
                                lastKnownLocation = location,
                                medicalNeeds = meds,
                                lastCheckInTime = System.currentTimeMillis()
                            )
                        )
                        memberToEdit = null
                    }
                ) {
                    Text("Update")
                }
            },
            dismissButton = {
                Button(onClick = { memberToEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Add Emergency Contact Dialog
    if (showAddContactDialog) {
        var name by remember { mutableStateOf("") }
        var phone by remember { mutableStateOf("") }
        var category by remember { mutableStateOf("Neighbor / Boat") }
        var notes by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddContactDialog = false },
            title = { Text("Add Emergency Contact", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Contact Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (e.g. Volunteer Boat, Doctor, Uncle)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Notes (e.g. Has 16ft flat bottom boat)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (name.isNotBlank() && phone.isNotBlank()) {
                            viewModel.addContact(
                                EmergencyContact(
                                    name = name,
                                    phone = phone,
                                    category = category,
                                    notes = notes
                                )
                            )
                            showAddContactDialog = false
                        }
                    }
                ) {
                    Text("Save Contact")
                }
            },
            dismissButton = {
                Button(onClick = { showAddContactDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun FamilyMemberCard(
    member: FamilyMember,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val statusColor = when (member.status) {
        "Safe" -> SafeGreen
        "Needs Help" -> DangerSosRed
        "Evacuated" -> RescueCyan
        else -> AmberEmergency
    }

    val dateFormat = remember { SimpleDateFormat("h:mm a, MMM dd", Locale.getDefault()) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, statusColor.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(statusColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = statusColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = statusColor.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = member.status.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Black,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = "Location: ${member.lastKnownLocation}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (member.medicalNeeds != "None") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.MedicalServices, contentDescription = null, tint = DangerSosRed, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Medical: ${member.medicalNeeds} (Blood: ${member.bloodType})",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = DangerSosRed
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Updated ${dateFormat.format(Date(member.lastCheckInTime))}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )

                Row {
                    IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Status", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun EmergencyContactCard(
    contact: EmergencyContact,
    onCall: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (contact.isPrimarySos) DangerSosRed.copy(alpha = 0.8f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                RoundedCornerShape(12.dp)
            )
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
                    if (contact.isPrimarySos) {
                        Surface(
                            color = DangerSosRed,
                            shape = RoundedCornerShape(4.dp),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "SOS",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Text(
                    text = "${contact.category} • ${contact.phone}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                if (contact.notes.isNotEmpty()) {
                    Text(
                        text = contact.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onCall, modifier = Modifier.size(42.dp)) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = "Call",
                        tint = SafeGreen,
                        modifier = Modifier.size(24.dp)
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
}
