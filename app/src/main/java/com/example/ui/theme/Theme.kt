package com.example.ui.theme

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

private val DarkColorScheme =
  darkColorScheme(
    primary = SageGreen,
    secondary = TeaGreen,
    tertiary = ForestGreen,
    background = EarthyDark,
    surface = Color(0xFF232D2A),
    onPrimary = WarmIvory,
    onSecondary = EarthyDark,
    onTertiary = WarmIvory,
    onBackground = WarmIvory,
    onSurface = WarmIvory
  )

private val LightColorScheme =
  lightColorScheme(
    primary = SageGreen,
    secondary = TeaGreen,
    tertiary = ForestGreen,
    background = WarmIvory,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = EarthyDark,
    onTertiary = Color.White,
    onBackground = EarthyDark,
    onSurface = EarthyDark
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Set default to false to prioritize our elegant Natural Tones theme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
