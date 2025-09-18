package com.jorgeromo.gym4ULSA.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC62828), // RedPrimary
    onPrimary = Color.White,
    secondary = Color(0xFF8B0A1A), // RedAccent
    onSecondary = Color.Black,
    tertiary = Color(0xFF7B1FA2), // RedPrimaryDark
    onTertiary = Color.White,
    background = Color(0xFF2A2A2A), // DarkGray
    onBackground = Color.White,
    surface = Color(0xFF2A2A2A), // SurfaceDark
    onSurface = Color.White,
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFAAAAAA) // LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFC62828), // RedPrimary
    onPrimary = Color.White,
    secondary = Color(0xFF8B0A1A), // RedAccent
    onSecondary = Color.White,
    tertiary = Color(0xFF7B1FA2), // RedPrimaryDark
    onTertiary = Color.White,
    background = Color(0xFFFDFDFD),
    onBackground = Color(0xFF111111),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111111),
    surfaceVariant = Color(0xFFF2F2F2),
    onSurfaceVariant = Color(0xFF333333)
)

@Composable
fun AndroidClassMP1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Force app branding instead of dynamic colors
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