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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.FloodAidViewModel
import com.example.ui.theme.AmberEmergency
import com.example.ui.theme.DangerSosRed
import com.example.ui.theme.RescueCyan
import com.example.ui.theme.SafeGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PersonCondition(val label: String, val chipColor: Color) {
    FINE("Fine / Safe", SafeGreen),
    INJURED("Injured", AmberEmergency),
    CRITICAL("Critical / Emergency", DangerSosRed),
    TRAPPED("Trapped in Water", Color(0xFF9C27B0)),
    NEEDS_MEDICINE("Needs Medicine", RescueCyan)
}

data class PersonEntry(
    var name: String = "",
    var condition: PersonCondition = PersonCondition.FINE,
    var details: String = ""
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatusSenderScreen(
    viewModel: FloodAidViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val gpsLocation by viewModel.currentLocation.collectAsState()
    val batteryPercent by viewModel.batteryPercent.collectAsState()

    val targetPhoneNumber = "8384034810"

    var peopleCount by remember { mutableIntStateOf(2) }
    val peopleList = remember {
        mutableStateListOf(
            PersonEntry("Self", PersonCondition.FINE, ""),
            PersonEntry("Family Member 2", PersonCondition.INJURED, "Minor cut")
        )
    }

    var lastSentTime by remember { mutableStateOf<String?>(null) }

    fun updatePeopleCount(newCount: Int) {
        val count = newCount.coerceIn(1, 25)
        peopleCount = count
        while (peopleList.size < count) {
            val num = peopleList.size + 1
            peopleList.add(PersonEntry("Person $num", PersonCondition.FINE, ""))
        }
        while (peopleList.size > count) {
            peopleList.removeAt(peopleList.lastIndex)
        }
    }

    // Build the SMS message text
    fun generateSmsReport(): String {
        val sb = StringBuilder()
        sb.append("[FLOOD-AID RESCUE STATUS REPORT]\n")
        sb.append("TARGET DESK: $targetPhoneNumber\n")
        sb.append("TOTAL PEOPLE: ${peopleList.size}\n\n")

        val fineCount = peopleList.count { it.condition == PersonCondition.FINE }
        val injuredCount = peopleList.count { it.condition == PersonCondition.INJURED }
        val criticalCount = peopleList.count { it.condition == PersonCondition.CRITICAL }
        val trappedCount = peopleList.count { it.condition == PersonCondition.TRAPPED }
        val medsCount = peopleList.count { it.condition == PersonCondition.NEEDS_MEDICINE }

        sb.append("SUMMARY:\n")
        if (criticalCount > 0) sb.append("- CRITICAL: $criticalCount\n")
        if (trappedCount > 0) sb.append("- TRAPPED: $trappedCount\n")
        if (injuredCount > 0) sb.append("- INJURED: $injuredCount\n")
        if (medsCount > 0) sb.append("- NEEDS MEDS: $medsCount\n")
        if (fineCount > 0) sb.append("- FINE / SAFE: $fineCount\n")

        sb.append("\nINDIVIDUAL STATUS:\n")
        peopleList.forEachIndexed { idx, p ->
            val pName = p.name.ifBlank { "Person ${idx + 1}" }
            val extra = if (p.details.isNotBlank()) " (${p.details})" else ""
            sb.append("${idx + 1}. $pName: ${p.condition.label}$extra\n")
        }

        sb.append("\nLOCATION:\n")
        if (gpsLocation != null) {
            sb.append("Lat: %.5f, Lon: %.5f\n".format(gpsLocation!!.latitude, gpsLocation!!.longitude))
            sb.append("Map: https://maps.google.com/?q=%.5f,%.5f\n".format(gpsLocation!!.latitude, gpsLocation!!.longitude))
        } else {
            sb.append("GPS: Waiting for satellite lock\n")
        }

        sb.append("Battery: $batteryPercent%\n")
        val timestamp = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()).format(Date())
        sb.append("Time: $timestamp")
        return sb.toString()
    }

    val reportText = generateSmsReport()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Stepper for choosing number of people
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "HOW MANY PEOPLE WITH YOU?",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { updatePeopleCount(peopleCount - 1) },
                            enabled = peopleCount > 1,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }

                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "$peopleCount",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(10.dp))

                        IconButton(
                            onClick = { updatePeopleCount(peopleCount + 1) },
                            enabled = peopleCount < 25,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                // Quick presets
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(1, 2, 3, 4, 5, 8, 10).forEach { preset ->
                        FilterChip(
                            selected = peopleCount == preset,
                            onClick = { updatePeopleCount(preset) },
                            label = { Text("$preset ${if (preset == 1) "Person" else "People"}", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }
            }
        }

        // Tally Summary Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val critical = peopleList.count { it.condition == PersonCondition.CRITICAL }
            val injured = peopleList.count { it.condition == PersonCondition.INJURED }
            val trapped = peopleList.count { it.condition == PersonCondition.TRAPPED }
            val fine = peopleList.count { it.condition == PersonCondition.FINE }

            if (critical > 0) {
                Surface(
                    color = DangerSosRed.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "$critical Critical",
                        color = DangerSosRed,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            if (injured > 0) {
                Surface(
                    color = AmberEmergency.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "$injured Injured",
                        color = AmberEmergency,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            if (trapped > 0) {
                Surface(
                    color = Color(0xFF9C27B0).copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "$trapped Trapped",
                        color = Color(0xFF9C27B0),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(6.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
            Surface(
                color = SafeGreen.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "$fine Safe/Fine",
                    color = SafeGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(6.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        // List of people cards
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(peopleList) { index, person ->
                PersonStatusCard(
                    index = index + 1,
                    entry = person,
                    onUpdate = { updated ->
                        peopleList[index] = updated
                    }
                )
            }
        }

        // Bottom Dispatch Action Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth().border(1.5.dp, DangerSosRed, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = DangerSosRed, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Destination: $targetPhoneNumber",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    if (lastSentTime != null) {
                        Text(
                            text = "Sent: $lastSentTime",
                            fontSize = 10.sp,
                            color = SafeGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("smsto:$targetPhoneNumber")
                                putExtra("sms_body", reportText)
                            }
                            try {
                                context.startActivity(smsIntent)
                                lastSentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                Toast.makeText(context, "Opening SMS to $targetPhoneNumber", Toast.LENGTH_SHORT).show()
                            } catch (_: Exception) {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Rescue Status", reportText))
                                Toast.makeText(context, "Copied status report to clipboard", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DangerSosRed),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(2f)
                            .height(44.dp)
                            .testTag("send_status_sms_btn")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("SEND SMS (8384034810)", fontWeight = FontWeight.Black, fontSize = 12.sp, color = Color.White)
                    }

                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Rescue Status", reportText))
                            Toast.makeText(context, "Status report copied", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("COPY", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonStatusCard(
    index: Int,
    entry: PersonEntry,
    onUpdate: (PersonEntry) -> Unit
) {
    var nameText by remember(entry.name) { mutableStateOf(entry.name) }
    var detailsText by remember(entry.details) { mutableStateOf(entry.details) }
    var currentCondition by remember(entry.condition) { mutableStateOf(entry.condition) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, currentCondition.chipColor.copy(alpha = 0.7f), RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(currentCondition.chipColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("$index", fontWeight = FontWeight.Black, fontSize = 12.sp, color = currentCondition.chipColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = nameText,
                        onValueChange = {
                            nameText = it
                            onUpdate(entry.copy(name = it))
                        },
                        placeholder = { Text("Person $index (e.g. Self, Mother)", fontSize = 12.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    )
                }
            }

            // Status Selector Chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                PersonCondition.values().forEach { cond ->
                    val isSelected = currentCondition == cond
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            currentCondition = cond
                            onUpdate(entry.copy(condition = cond))
                        },
                        label = { Text(cond.label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = cond.chipColor,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Injury / Notes details field
            OutlinedTextField(
                value = detailsText,
                onValueChange = {
                    detailsText = it
                    onUpdate(entry.copy(details = it))
                },
                placeholder = { Text("Condition / needs (e.g. Leg fracture, asthma, dehydrated)", fontSize = 11.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
            )
        }
    }
}
