import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import di.KoinInitializer

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    KoinInitializer().init()
    CanvasBasedWindow(
        canvasElementId = "ComposeTarget",
        title = "WoWo"
    ) {
        WebApp()
    }
}