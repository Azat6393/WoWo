import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import presentation.component.ui.WoWoTheme
import di.KoinInitializer
import di.viewModelModule
import di.domainModule
import org.koin.compose.KoinApplication
import org.koin.compose.KoinContext
import org.koin.compose.koinInject
import presentation.game.GameEvent
import presentation.game.GameScreen
import presentation.game.GameViewModel

@Composable
fun WebApp() {
    WoWoTheme {
        KoinApplication(
            application = { KoinInitializer().init() }
        ) {
            val viewModel = koinInject<GameViewModel>()
            val state = viewModel.state

            LaunchedEffect(Unit) {
                viewModel.onEvent(
                    GameEvent.OnLanguageChange("eng")
                )
            }

            GameScreen(
                modifier = Modifier.fillMaxSize(),
                state = state,
                onEvent = viewModel::onEvent
            )
        }
    }
}