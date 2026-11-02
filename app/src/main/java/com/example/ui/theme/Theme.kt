package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DisasterDarkColorScheme = darkColorScheme(
    primary = AmberEmergency,
    onPrimary = Color.Black,
    primaryContainer = AmberEmergencyDark,
    onPrimaryContainer = Color.White,
    secondary = RescueCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF004D61),
    onSecondaryContainer = Color.White,
    tertiary = SafeGreen,
    onTertiary = Color.Black,
    tertiaryContainer = SafeGreenDark,
    onTertiaryContainer = Color.White,
    error = DangerSosRed,
    onError = Color.White,
    errorContainer = DangerSosDark,
    onErrorContainer = Color.White,
    background = DeepMidnightBg,
    onBackground = TextWhiteHighContrast,
    surface = DeepMidnightSurface,
    onSurface = TextWhiteHighContrast,
    surfaceVariant = DeepMidnightSurfaceVariant,
    onSurfaceVariant = Color(0xFFCFD8DC),
    outline = DeepMidnightBorder
)

private val BlackoutOledColorScheme = darkColorScheme(
    primary = AmberEmergency,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF332200),
    onPrimaryContainer = AmberEmergency,
    secondary = RescueCyan,
    onSecondary = Color.Black,
    tertiary = SafeGreen,
    onTertiary = Color.Black,
    error = DangerSosRed,
    onError = Color.White,
    background = OledBlack,
    onBackground = Color.White,
    surface = OledBlackSurface,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF141414),
    onSurfaceVariant = Color(0xFFB0BEC5),
    outline = OledBorder
)

private val DisasterLightColorScheme = lightColorScheme(
    primary = Color(0xFFC65102),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDBCF),
    onPrimaryContainer = Color(0xFF380D00),
    secondary = Color(0xFF00687A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB5E4F0),
    onSecondaryContainer = Color(0xFF001F26),
    tertiary = Color(0xFF1B6C3D),
    onTertiary = Color.White,
    error = DangerSosDark,
    onError = Color.White,
    background = DaylightBg,
    onBackground = DaylightTextPrimary,
    surface = DaylightSurface,
    onSurface = DaylightTextPrimary,
    surfaceVariant = DaylightSurfaceVariant,
    onSurfaceVariant = DaylightTextSecondary,
    outline = Color(0xFF94A3B8)
)

@Composable
fun FloodAidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isBlackoutMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isBlackoutMode -> BlackoutOledColorScheme
        darkTheme -> DisasterDarkColorScheme
        else -> DisasterLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
