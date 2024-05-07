package presentation.game

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import component.HowToPlayDialog
import component.Keyboard
import component.MessageBar
import component.TopBar
import component.WordContainer
import component.ui.ColorBackground
import component.ui.ColorOnBackground
import component.ui.ColorPrimary
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.game.component.CustomAlertDialog
import presentation.game.component.GameConditionContainer
import presentation.game.component.GameResultContainer
import presentation.game.component.QuestionResultContainer
import presentation.game.component.QuestionTextField
import presentation.game.component.StartGameContainer
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.close_icon
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.get_letter
import wowo.composeapp.generated.resources.give_up
import wowo.composeapp.generated.resources.give_up_text
import wowo.composeapp.generated.resources.give_up_title
import wowo.composeapp.generated.resources.tips_icon


@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    state: GameState,
    onEvent: (GameEvent) -> Unit,
    showRewardedAd: () -> Unit = {},
    share: () -> Unit = {}
) {
    var showGiveUpDialog by remember { mutableStateOf(false) }
    var showHowToPlayDialog by remember { mutableStateOf(false) }

    if (showGiveUpDialog) {
        CustomAlertDialog(
            onDismissRequest = { showGiveUpDialog = false },
            onConfirmation = {
                showGiveUpDialog = false
                onEvent(GameEvent.GiveUp)
            },
            dialogText = stringResource(Res.string.give_up_text),
            dialogTitle = stringResource(Res.string.give_up_title)
        )
    }

    if (showHowToPlayDialog) {
        HowToPlayDialog { showHowToPlayDialog = false }
    }

    Column(
        modifier = modifier
            .background(ColorBackground)
    ) {
        TopBar(
            modifier = Modifier.fillMaxWidth(),
            showHowToPlay = { showHowToPlayDialog = true }
        )
        MessageBar(messageBarState = state.message)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(1f)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(40.dp))
                WordContainer(
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                    wordLetters = state.word
                )
                Spacer(Modifier.height(35.dp))
                GameConditionContainer(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp),
                    gameCondition = state.gameConditionsUI,
                    language = state.gameSettings.selectedLanguage
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(1f)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                GameButtons(
                    onGiveUp = { showGiveUpDialog = true },
                    onGetLetter = showRewardedAd
                )
                Spacer(Modifier.height(30.dp))
                QuestionResultContainer(
                    modifier = Modifier.fillMaxWidth(),
                    aiResult = state.aiResult,
                    language = state.gameSettings.selectedLanguage
                )
                Spacer(Modifier.height(20.dp))
                QuestionTextField(
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                    text = state.question,
                    onAskQuestion = { onEvent(GameEvent.AskQuestion) },
                    isAskQuestionEnable = state.isQuestionEnable,
                    isLoading = state.aiLoading,
                    onTextChange = { onEvent(GameEvent.OnQuestionInput(it)) },
                    language = state.gameSettings.selectedLanguage
                )
                Spacer(Modifier.height(30.dp))
                Keyboard(
                    modifier = Modifier.fillMaxWidth(),
                    onInputValue = { onEvent(GameEvent.OnInputLetter(it)) },
                    onDeleteValue = { onEvent(GameEvent.OnDeleteLetter) },
                    onEnter = { onEvent(GameEvent.Enter) },
                    isEnterEnable = state.isEnterEnable,
                    notInWordLetters = state.notInWordLetters,
                    language = state.gameSettings.selectedLanguage
                )
                Spacer(Modifier.height(15.dp))
            }
            if (!state.isGameStarted) {
                StartGameContainer(
                    onStart = { onEvent(GameEvent.StartGame) },
                    gameSettings = state.gameSettings,
                    onCategorySelect = { onEvent(GameEvent.OnCategorySelect(it)) },
                    onDifficultySelect = { onEvent(GameEvent.OnDifficultyChange(it)) },
                    onLanguageSelect = { onEvent(GameEvent.OnLanguageChange(it)) },
                    howToPlay = { showHowToPlayDialog = true }
                )
            }
            if (state.gameResult != null) {
                GameResultContainer(
                    playAgain = { onEvent(GameEvent.StartGame) },
                    gameResult = state.gameResult,
                    gameSettings = state.gameSettings,
                    actualWord = state.actualWord,
                    onCategorySelect = { onEvent(GameEvent.OnCategorySelect(it)) },
                    onDifficultySelect = { onEvent(GameEvent.OnDifficultyChange(it)) },
                    onLanguageSelect = { onEvent(GameEvent.OnLanguageChange(it)) },
                    gameCondition = state.gameConditionsUI,
                    share = share
                )
            }
            if (state.loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(60.dp).align(Alignment.Center),
                    color = ColorPrimary
                )
            }
        }
    }
}

@Composable
private fun GameButtons(
    onGiveUp: () -> Unit,
    onGetLetter: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box {
            Button(
                onClick = onGetLetter,
                colors = ButtonDefaults.buttonColors(
                    contentColor = ColorOnBackground,
                    backgroundColor = ColorBackground
                ),
                shape = RoundedCornerShape(30.dp),
                contentPadding = PaddingValues(10.dp, 2.dp, 45.dp, 2.dp),
                modifier = Modifier.align(Alignment.CenterEnd),
                content = {
                    Text(
                        text = stringResource(Res.string.get_letter),
                        fontFamily = FontFamily(Font(Res.font.geologica_regular))
                    )
                }
            )
            Image(
                painter = painterResource(Res.drawable.tips_icon),
                contentDescription = null,
                modifier = Modifier.size(35.dp).align(Alignment.CenterEnd)
            )
        }
        Spacer(Modifier.width(30.dp))
        Box {
            Button(
                onClick = onGiveUp,
                colors = ButtonDefaults.buttonColors(
                    contentColor = ColorOnBackground,
                    backgroundColor = ColorBackground
                ),
                shape = RoundedCornerShape(30.dp),
                contentPadding = PaddingValues(10.dp, 2.dp, 45.dp, 2.dp),
                modifier = Modifier.align(Alignment.CenterEnd),
                content = {
                    Text(
                        text = stringResource(Res.string.give_up),
                        fontFamily = FontFamily(Font(Res.font.geologica_regular))
                    )
                }
            )
            Image(
                painter = painterResource(Res.drawable.close_icon),
                contentDescription = null,
                modifier = Modifier.size(35.dp).align(Alignment.CenterEnd)
            )
        }
    }
}

