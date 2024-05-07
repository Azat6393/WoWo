package com.caelum.wowo.ui

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import component.ui.ColorBackground
import component.ui.ColorOnBackground
import component.ui.ColorPrimary
import component.ui.ColorSecondary


private val DarkColorScheme = lightColors(
    primary = ColorPrimary,
    secondary = ColorSecondary,
    background = ColorBackground,
    onBackground = ColorOnBackground
)

private val LightColorScheme = darkColors(
    primary = ColorPrimary,
    secondary = ColorSecondary,
    background = ColorBackground,
    onBackground = ColorOnBackground
)

@Composable
fun WoWoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colors = colorScheme,
        content = content
    )
}