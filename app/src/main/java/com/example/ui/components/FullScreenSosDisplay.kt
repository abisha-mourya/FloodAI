package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DangerSosRed
import com.example.util.GpsLocation
import kotlinx.coroutines.delay

@Composable
fun FullScreenSosDisplay(
    gpsLocation: GpsLocation?,
    batteryPercent: Int,
    isSirenActive: Boolean,
    onToggleSiren: () -> Unit,
    onDismiss: () -> Unit
) {
    var flashState by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            flashState = !flashState
            delay(500)
        }
    }

    val bgColor = if (flashState) Color(0xFFFFCC00) else Color(0xFFD50000)
    val textColor = if (flashState) Color.Black else Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(24.dp)
    ) {
        // Dismiss close button top right
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Exit SOS Board",
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header
            Text(
                text = "EMERGENCY RESCUE",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = textColor,
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )

            // Massive SOS
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "SOS",
                    fontSize = 110.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor,
                    letterSpacing = 8.sp,
                    lineHeight = 110.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "RESCUE NEEDED HERE",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }

            // Coordinates box
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "EXACT GPS POSITION",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFCC00)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (gpsLocation != null) {
                        "%.5f° N\n%.5f° W".format(gpsLocation.latitude, -gpsLocation.longitude)
                    } else {
                        "GPS ACQUIRING..."
                    },
                    fontSize = 24.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "BATTERY: $batteryPercent% • SHOW SCREEN TO BOATS / DRONES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            // Bottom Siren Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onToggleSiren,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSirenActive) Color.White else Color.Black,
                        contentColor = if (isSirenActive) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = if (isSirenActive) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isSirenActive) "MUTE ALARM" else "SOUND ALARM",
                        fontWeight = FontWeight.Black
                    )
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black.copy(alpha = 0.6f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text(text = "CLOSE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
