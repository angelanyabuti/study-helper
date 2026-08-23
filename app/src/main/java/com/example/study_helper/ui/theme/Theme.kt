package com.example.study_helper.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// The app is designed around one deliberate dark, blue-accented look
// (see InputScreen), so both schemes share the same identity instead of
// falling back to unrelated Material defaults.
private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    primaryContainer = AccentBlueDark,
    onPrimaryContainer = TextPrimary,
    secondary = AccentBlueLight,
    onSecondary = BackgroundDark,
    background = BackgroundDark,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceDarkAlt,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark,
    error = ErrorRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,
    primaryContainer = AccentBlueBg,
    onPrimaryContainer = AccentBlueDark,
    secondary = AccentBlueDark,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = Color(0xFF1A1A2E),
    surface = Color(0xFFF8F8F8),
    onSurface = Color(0xFF1A1A2E),
    surfaceVariant = AccentBlueBg,
    onSurfaceVariant = AccentBlueDark,
    outline = AccentBlueBorder,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun Study_helperTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
