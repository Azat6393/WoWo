package mobile_ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import di.commonModule
import di.domainModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import presentation.game.GameScreen

@Composable
@Preview
fun MobileApp() {
    MaterialTheme {
        KoinApplication(application = {
            modules(domainModule, commonModule)
        }) {
            GameScreen()
        }
    }
}
