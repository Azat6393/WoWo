package presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.GameConditionContainer
import component.Keyboard
import component.MessageBar
import component.QuestionResultContainer
import component.QuestionTextField
import component.TopBar
import component.WordContainer
import component.ui.ColorGreen
import component.ui.ColorOnBackground
import component.ui.ColorPrimary
import component.ui.ColorRed
import domain.model.Category
import org.koin.compose.koinInject
import presentation.Strings


@Composable
fun GameScreen(isWeb: Boolean = false) {
    val viewModel = koinInject<GameViewModel>()
    val state = viewModel.state

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        TopBar(modifier = Modifier.fillMaxWidth())
        MessageBar(messageBarState = state.message)
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(if (isWeb) 0.6f else 1f)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(40.dp))
                WordContainer(
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                    wordLetters = state.word,
                    isWeb = isWeb
                )
                Spacer(Modifier.height(35.dp))
                GameConditionContainer(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp),
                    gameCondition = state.gameConditionsUI,
                    isWeb = isWeb,
                    language = state.gameSettings.selectedLanguage
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(if (isWeb) 0.6f else 1f)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                QuestionResultContainer(
                    modifier = Modifier.fillMaxWidth(),
                    aiResult = state.aiResult,
                    isWeb = isWeb,
                    language = state.gameSettings.selectedLanguage
                )
                Spacer(Modifier.height(30.dp))
                QuestionTextField(
                    modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                    text = state.question,
                    onAskQuestion = { viewModel.onEvent(GameEvent.AskQuestion) },
                    isAskQuestionEnable = state.isQuestionEnable,
                    isLoading = state.aiLoading,
                    onTextChange = { viewModel.onEvent(GameEvent.OnQuestionInput(it)) },
                    language = state.gameSettings.selectedLanguage
                )
                Spacer(Modifier.height(40.dp))
                Keyboard(
                    modifier = Modifier.fillMaxWidth(),
                    onInputValue = { viewModel.onEvent(GameEvent.OnInputLetter(it)) },
                    onDeleteValue = { viewModel.onEvent(GameEvent.OnDeleteLetter) },
                    onEnter = { viewModel.onEvent(GameEvent.Enter) },
                    isEnterEnable = state.isEnterEnable,
                    notInWordLetters = state.notInWordLetters,
                    language = state.gameSettings.selectedLanguage
                )
                Spacer(Modifier.height(15.dp))
            }
            if (!state.isGameStarted) {
                StartGameContainer(
                    onStart = { viewModel.onEvent(GameEvent.StartGame) },
                    gameSettings = state.gameSettings,
                    onCategorySelect = { viewModel.onEvent(GameEvent.OnCategorySelect(it)) },
                    onDifficultySelect = { viewModel.onEvent(GameEvent.OnDifficultyChange(it)) },
                    onLanguageSelect = { viewModel.onEvent(GameEvent.OnLanguageChange(it)) }
                )
            }
            if (state.gameResult != null) {
                GameResultContainer(
                    playAgain = { viewModel.onEvent(GameEvent.StartGame) },
                    gameResult = state.gameResult,
                    gameSettings = state.gameSettings,
                    actualWord = state.actualWord,
                    onCategorySelect = { viewModel.onEvent(GameEvent.OnCategorySelect(it)) },
                    onDifficultySelect = { viewModel.onEvent(GameEvent.OnDifficultyChange(it)) },
                    onLanguageSelect = { viewModel.onEvent(GameEvent.OnLanguageChange(it)) }
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
private fun GameResultContainer(
    modifier: Modifier = Modifier,
    playAgain: () -> Unit,
    gameResult: GameResult,
    gameSettings: GameSettings,
    onLanguageSelect: (String) -> Unit,
    onDifficultySelect: (Difficulty) -> Unit,
    onCategorySelect: (Category) -> Unit,
    actualWord: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorOnBackground.copy(alpha = 0.8f))
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (gameResult == GameResult.Win) Strings.win(gameSettings.selectedLanguage)
            else Strings.lose(gameSettings.selectedLanguage),
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = actualWord,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (gameResult == GameResult.Win) ColorGreen else ColorRed
        )
        Spacer(Modifier.height(30.dp))
        SettingContainer(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp),
            gameSettings = gameSettings,
            onLanguageSelect = onLanguageSelect,
            onDifficultySelect = onDifficultySelect,
            onCategorySelect = onCategorySelect
        )
        Spacer(Modifier.height(30.dp))
        Button(
            onClick = playAgain,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorPrimary,
                contentColor = ColorOnBackground
            ),
            contentPadding = PaddingValues(start = 30.dp, end = 30.dp, top = 7.dp, bottom = 7.dp),
            content = {
                Text(Strings.playAgain(gameSettings.selectedLanguage), fontSize = 20.sp)
            }
        )
    }
}

@Composable
private fun StartGameContainer(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    gameSettings: GameSettings,
    onLanguageSelect: (String) -> Unit,
    onDifficultySelect: (Difficulty) -> Unit,
    onCategorySelect: (Category) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ColorOnBackground.copy(alpha = 0.8f))
            .clickable { },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        SettingContainer(
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp),
            gameSettings = gameSettings,
            onLanguageSelect = onLanguageSelect,
            onDifficultySelect = onDifficultySelect,
            onCategorySelect = onCategorySelect
        )
        Spacer(Modifier.height(30.dp))
        Button(
            onClick = onStart,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = ColorPrimary,
                contentColor = ColorOnBackground
            ),
            contentPadding = PaddingValues(start = 30.dp, end = 30.dp, top = 7.dp, bottom = 7.dp),
            content = {
                Text(Strings.play(gameSettings.selectedLanguage), fontSize = 20.sp)
            }
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SettingContainer(
    modifier: Modifier = Modifier,
    gameSettings: GameSettings,
    onLanguageSelect: (String) -> Unit,
    onDifficultySelect: (Difficulty) -> Unit,
    onCategorySelect: (Category) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(Strings.language(gameSettings.selectedLanguage), color = Color.White, fontSize = 20.sp)
        Spacer(Modifier.height(5.dp))
        FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            gameSettings.languages.forEach { language ->
                Button(
                    onClick = { onLanguageSelect(language) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = if (language == gameSettings.selectedLanguage) ColorPrimary else Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (language == gameSettings.selectedLanguage) ColorPrimary else Color.White
                    ),
                    content = { Text(language) }
                )
                Spacer(Modifier.width(10.dp))
            }
        }
        Spacer(Modifier.height(30.dp))
        Text(Strings.category(gameSettings.selectedLanguage), color = Color.White, fontSize = 20.sp)
        Spacer(Modifier.height(5.dp))
        FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            gameSettings.categories.forEach { category ->
                Button(
                    onClick = { onCategorySelect(category) },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent,
                        contentColor = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorPrimary else Color.White
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (gameSettings.selectedCategory?.uuid == category.uuid) ColorPrimary else Color.White
                    ),
                    content = { Text(category.name) }
                )
                Spacer(Modifier.width(10.dp))
            }
        }
        Spacer(Modifier.height(30.dp))
        Text(Strings.difficulty(gameSettings.selectedLanguage), color = Color.White, fontSize = 20.sp)
        Spacer(Modifier.height(5.dp))
        FlowRow(
            verticalArrangement = Arrangement.Center,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { onDifficultySelect(Difficulty.Easy) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = if (gameSettings.difficulty == Difficulty.Easy) ColorPrimary else Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (gameSettings.difficulty == Difficulty.Easy) ColorPrimary else Color.White
                ),
                content = { Text(Strings.easy(gameSettings.selectedLanguage)) }
            )
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = { onDifficultySelect(Difficulty.Medium) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = if (gameSettings.difficulty == Difficulty.Medium) ColorPrimary else Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (gameSettings.difficulty == Difficulty.Medium) ColorPrimary else Color.White
                ),
                content = { Text(Strings.medium(gameSettings.selectedLanguage)) }
            )
            Spacer(Modifier.width(10.dp))
            Button(
                onClick = { onDifficultySelect(Difficulty.Hard) },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = if (gameSettings.difficulty == Difficulty.Hard) ColorPrimary else Color.White
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (gameSettings.difficulty == Difficulty.Hard) ColorPrimary else Color.White
                ),
                content = { Text(Strings.hard(gameSettings.selectedLanguage)) }
            )
        }
    }
}
