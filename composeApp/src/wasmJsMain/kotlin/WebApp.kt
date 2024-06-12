import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import org.koin.compose.koinInject
import presentation.component.ui.WoWoTheme
import presentation.game.GameEvent
import presentation.game.GameScreen
import presentation.game.GameViewModel

@Composable
fun WebApp() {
    WoWoTheme {
        val viewModel = koinInject<GameViewModel>()
        val state = viewModel.state

        LaunchedEffect(Unit) {
            viewModel.onEvent(
                GameEvent.OnLanguageChange("eng")
            )
        }

        GameScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}