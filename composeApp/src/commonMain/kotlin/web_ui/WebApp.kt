package web_ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.dp
import di.commonModule
import di.domainModule
import org.koin.compose.KoinApplication
import presentation.game.GameScreen
import presentation.pxToDp

@Composable
fun WebApp() {
    MaterialTheme {
        KoinApplication(application = {
            modules(domainModule, commonModule)
        }) {
            var isWeb by remember { mutableStateOf(false) }

            Layout(
                content = {
                    GameScreen(isWeb = true)
                },
                measurePolicy = { measurables, constraints ->
                    val width = constraints.maxWidth
                    val height = constraints.maxHeight

                    isWeb = width > 600.dp.toPx()

                    val placeables = measurables.map { measurable ->
                        measurable.measure(constraints)
                    }

                    layout(width, height) {
                        var yPosition = 0
                        placeables.forEach { placeable ->
                            placeable.placeRelative(x = 0, y = yPosition)
                            yPosition += placeable.height
                        }
                    }
                }
            )
        }
    }
}