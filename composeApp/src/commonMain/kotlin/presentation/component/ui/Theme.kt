package presentation.component.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable


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

    MaterialTheme(
        colors = colorScheme,
        content = content
    )
}