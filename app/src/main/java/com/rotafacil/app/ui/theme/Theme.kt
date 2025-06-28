package com.rotafacil.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueDark,
    onPrimary = OnPrimaryDark,
    primaryContainer = PrimaryBlueVariantDark,
    onPrimaryContainer = OnPrimaryDark,
    secondary = SecondaryGreenDark,
    onSecondary = OnSecondaryDark,
    secondaryContainer = SecondaryGreenVariantDark,
    onSecondaryContainer = OnSecondaryDark,
    tertiary = AccentOrangeDark,
    onTertiary = OnPrimaryDark,
    tertiaryContainer = AccentOrangeVariantDark,
    onTertiaryContainer = OnPrimaryDark,
    background = BackgroundDark,
    onBackground = OnSurfaceDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    error = ErrorRed,
    onError = OnPrimaryLight,
    errorContainer = ErrorRed.copy(alpha = 0.2f),
    onErrorContainer = OnPrimaryLight
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = OnPrimaryLight,
    primaryContainer = PrimaryBlueVariant,
    onPrimaryContainer = OnPrimaryLight,
    secondary = SecondaryGreen,
    onSecondary = OnSecondaryLight,
    secondaryContainer = SecondaryGreenVariant,
    onSecondaryContainer = OnSecondaryLight,
    tertiary = AccentOrange,
    onTertiary = OnPrimaryLight,
    tertiaryContainer = AccentOrangeVariant,
    onTertiaryContainer = OnPrimaryLight,
    background = BackgroundLight,
    onBackground = OnSurfaceLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    error = ErrorRed,
    onError = OnPrimaryLight,
    errorContainer = ErrorRed.copy(alpha = 0.1f),
    onErrorContainer = ErrorRed
)

@Composable
fun RotaFacilTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Desabilitado para usar nossas cores customizadas
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Status bar transparente para um visual mais moderno
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
} 