package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashlightOff
import androidx.compose.material.icons.filled.FlashlightOn
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.FloodAidViewModel
import com.example.ui.components.LargeDisasterButton
import com.example.ui.components.OutlinedDisasterButton
import com.example.ui.theme.AmberEmergency
import com.example.ui.theme.DangerSosRed
import com.example.ui.theme.SafeGreen

@Composable
fun SosRescueScreen(
    viewModel: FloodAidViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isStrobeActive by viewModel.isStrobeActive.collectAsState()
    val isSirenActive by viewModel.isSirenActive.collectAsState()
    val isFlashlightOn by viewModel.isFlashlightOn.collectAsState()
    val gpsLocation by viewModel.currentLocation.collectAsState()
    val batteryPercent by viewModel.batteryPercent.collectAsState()

    var peopleCount by remember { mutableIntStateOf(2) }
    var selectedWaterLevel by remember { mutableStateOf("1st Floor Flooded") }
    var medicalNote by remember { mutableStateOf("Need dry shelter") }
    val waterLevelOptions = listOf("Ankle / Knee Deep", "1st Floor Flooded", "Trapped in Attic", "Trapped on Roof")

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero visual banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.img_flood_rescue_hero_1790867887125),
                    contentDescription = "Flood rescue illustration",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 8.5f)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(14.dp)
                ) {
                    Text(
                        text = "EMERGENCY RESCUE HUB",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                    Text(
                        text = "Works offline without cellular towers or internet",
                        style = MaterialTheme.typography.bodySmall,
                        color = AmberEmergency,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Primary SOS Beacon Block (Massive high visibility buttons)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = DangerSosRed.copy(alpha = 0.15f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, DangerSosRed, RoundedCornerShape(16.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = DangerSosRed,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DISTRESS BEACONS",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            color = DangerSosRed
                        )
                    }
                    if (isStrobeActive || isSirenActive) {
                        Text(
                            text = "TRANSMITTING",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Black,
                            color = DangerSosRed
                        )
                    }
                }

                Text(
                    text = "Activate high-intensity audio and light signals to guide rescue boats, drones, and helicopters directly to your location.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Strobe Flashlight Button
                LargeDisasterButton(
                    text = if (isStrobeActive) "STOP STROBE SOS" else "SOS STROBE FLASHLIGHT",
                    subtext = "High-intensity Morse code (... --- ...) strobe",
                    icon = if (isStrobeActive) Icons.Default.FlashlightOff else Icons.Default.FlashlightOn,
                    backgroundColor = if (isStrobeActive) AmberEmergency else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isStrobeActive) Color.Black else MaterialTheme.colorScheme.onSurface,
                    onClick = { viewModel.toggleStrobe() },
                    testTag = "sos_strobe_btn"
                )

                // High Decibel Siren Button
                LargeDisasterButton(
                    text = if (isSirenActive) "STOP ACOUSTIC ALARM" else "SOUND DISTRESS WHISTLE",
                    subtext = "Penetrating emergency siren audible through storm wind",
                    icon = if (isSirenActive) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                    backgroundColor = if (isSirenActive) DangerSosRed else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = Color.White,
                    onClick = { viewModel.toggleSiren() },
                    testTag = "sos_siren_btn"
                )

                // Fullscreen Board Button
                OutlinedDisasterButton(
                    text = "SHOW FULL-SCREEN SOS BOARD",
                    icon = Icons.Default.Visibility,
                    borderColor = AmberEmergency,
                    contentColor = AmberEmergency,
                    onClick = { viewModel.showFullScreenSos(true) },
                    testTag = "sos_fullscreen_btn"
                )
            }
        }

        // Offline Rescue SMS Generator
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "SMS RESCUE PUNCH-THROUGH",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "When internet and voice fail, SMS texts often route through 2G queues. Tap to send formatted coordinates to rescue dispatchers.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // People count selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "People Trapped With You:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(4.dp)
                    ) {
                        IconButton(
                            onClick = { if (peopleCount > 1) peopleCount-- },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = "Decrease")
                        }
                        Text(
                            text = "$peopleCount",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )
                        IconButton(
                            onClick = { peopleCount++ },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Increase")
                        }
                    }
                }

                // Water level chips
                Text(
                    text = "Current Water Level:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    waterLevelOptions.take(2).forEach { level ->
                        FilterChip(
                            selected = selectedWaterLevel == level,
                            onClick = { selectedWaterLevel = level },
                            label = { Text(level, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AmberEmergency,
                                selectedLabelColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    waterLevelOptions.takeLast(2).forEach { level ->
                        FilterChip(
                            selected = selectedWaterLevel == level,
                            onClick = { selectedWaterLevel = level },
                            label = { Text(level, fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = DangerSosRed,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Medical / Special needs field
                OutlinedTextField(
                    value = medicalNote,
                    onValueChange = { medicalNote = it },
                    label = { Text("Medical Needs (Insulin, Oxygen, Elderly, Infant)") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                // Message preview
                val generatedMsg = viewModel.generateRescueSmsMessage(
                    peopleCount = peopleCount,
                    waterDepthDesc = selectedWaterLevel,
                    medicalUrgency = medicalNote
                )

                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "PREVIEW SMS PAYLOAD:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = generatedMsg,
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Action buttons: Send SMS and Copy
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    LargeDisasterButton(
                        text = "DISPATCH SMS",
                        icon = Icons.Default.Send,
                        backgroundColor = DangerSosRed,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1.5f),
                        onClick = {
                            val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("smsto:8384034810")
                                putExtra("sms_body", generatedMsg)
                            }
                            try {
                                context.startActivity(smsIntent)
                            } catch (_: Exception) {
                                Toast.makeText(context, "No SMS app found. Copied to clipboard.", Toast.LENGTH_SHORT).show()
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("Rescue SMS", generatedMsg))
                            }
                        },
                        testTag = "send_rescue_sms_btn"
                    )

                    OutlinedDisasterButton(
                        text = "COPY",
                        icon = Icons.Default.ContentCopy,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Rescue SMS", generatedMsg))
                            Toast.makeText(context, "SOS message copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        testTag = "copy_rescue_sms_btn"
                    )
                }
            }
        }

        // Quick Offline Emergency Callers (Direct dial intent)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "DIRECT EMERGENCY HOTLINES (INDIA)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface
                )

                EmergencyDialRow(
                    title = "112 National Emergency Helpline (Police / Fire / Medical)",
                    number = "112",
                    color = DangerSosRed
                ) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                    context.startActivity(intent)
                }

                EmergencyDialRow(
                    title = "1078 NDRF Disaster Control Room (HQ Delhi)",
                    number = "1078 / 011-24363260",
                    color = DangerSosRed
                ) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1078"))
                    context.startActivity(intent)
                }

                EmergencyDialRow(
                    title = "108 Emergency Ambulance / Medical Response",
                    number = "108",
                    color = AmberEmergency
                ) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:108"))
                    context.startActivity(intent)
                }

                EmergencyDialRow(
                    title = "1070 State Disaster Management Authority (SDMA)",
                    number = "1070",
                    color = AmberEmergency
                ) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1070"))
                    context.startActivity(intent)
                }

                EmergencyDialRow(
                    title = "1077 District Emergency Operation Center (DEOC)",
                    number = "1077",
                    color = SafeGreen
                ) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1077"))
                    context.startActivity(intent)
                }

                EmergencyDialRow(
                    title = "1554 Indian Coast Guard (Flood Air / Boat SAR)",
                    number = "1554",
                    color = SafeGreen
                ) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1554"))
                    context.startActivity(intent)
                }

                EmergencyDialRow(
                    title = "101 Fire and Rescue Control",
                    number = "101",
                    color = AmberEmergency
                ) {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:101"))
                    context.startActivity(intent)
                }

                EmergencyDialRow(
                    title = "Disaster SMS Rescue Desk Direct",
                    number = "8384034810",
                    color = DangerSosRed
                ) {
                    val intent = Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:8384034810"))
                    context.startActivity(intent)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun EmergencyDialRow(
    title: String,
    number: String,
    color: Color,
    onDial: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable(onClick = onDial)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = number,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                fontWeight = FontWeight.Black
            )
        }
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Call $number",
            tint = color,
            modifier = Modifier.size(24.dp)
        )
    }
}
