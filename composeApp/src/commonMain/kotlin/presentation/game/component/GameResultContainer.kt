package presentation.game.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.SettingContainer
import component.WordContainer
import component.WordStyleSmall
import component.ui.ColorOnBackground
import domain.model.Category
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import presentation.game.Difficulty
import presentation.game.GameConditionsUI
import presentation.game.GameResult
import presentation.game.GameSettings
import utils.generateWordLetterUI
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_semibold
import wowo.composeapp.generated.resources.lose
import wowo.composeapp.generated.resources.lose_icon
import wowo.composeapp.generated.resources.play
import wowo.composeapp.generated.resources.play_again
import wowo.composeapp.generated.resources.play_icon
import wowo.composeapp.generated.resources.share
import wowo.composeapp.generated.resources.win
import wowo.composeapp.generated.resources.win_icon

@Composable
fun GameResultContainer(
    modifier: Modifier = Modifier,
    playAgain: () -> Unit,
    gameResult: GameResult,
    gameSettings: GameSettings,
    onLanguageSelect: (String) -> Unit,
    onDifficultySelect: (Difficulty) -> Unit,
    onCategorySelect: (Category) -> Unit,
    actualWord: String,
    gameCondition: GameConditionsUI,
    share: () -> Unit
) {
    var showGameSettings by remember { mutableStateOf(false) }

    CustomDialogLayout(modifier = modifier) {
        if (!showGameSettings) {
            GameResultInfo(
                gameResult = gameResult,
                gameSettings = gameSettings,
                actualWord = actualWord,
                gameCondition = gameCondition,
                modifier = Modifier.fillMaxWidth(),
                playAgain = { showGameSettings = true },
                share = share
            )
        } else {
            PlayAgainContainer(
                gameSettings = gameSettings,
                onLanguageSelect = onLanguageSelect,
                onDifficultySelect = onDifficultySelect,
                onCategorySelect = onCategorySelect,
                modifier = Modifier.fillMaxWidth(),
                playAgain = playAgain
            )
        }
    }
}

@Composable
private fun PlayAgainContainer(
    gameSettings: GameSettings,
    onLanguageSelect: (String) -> Unit,
    onDifficultySelect: (Difficulty) -> Unit,
    onCategorySelect: (Category) -> Unit,
    modifier: Modifier,
    playAgain: () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.play_icon),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        SettingContainer(
            modifier = Modifier.fillMaxWidth(),
            gameSettings = gameSettings,
            onLanguageSelect = onLanguageSelect,
            onDifficultySelect = onDifficultySelect,
            onCategorySelect = onCategorySelect
        )
        Spacer(Modifier.height(30.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(), color = ColorOnBackground,
            thickness = 1.dp, startIndent = 1.dp
        )
        Spacer(Modifier.height(45.dp))
        CustomButton(
            modifier = modifier.fillMaxWidth(0.5f),
            text = stringResource(Res.string.play),
            onClick = playAgain
        )
    }
}

@Composable
private fun GameResultInfo(
    gameResult: GameResult,
    gameSettings: GameSettings,
    actualWord: String,
    gameCondition: GameConditionsUI,
    modifier: Modifier,
    playAgain: () -> Unit,
    share: () -> Unit
) {
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                if (gameResult == GameResult.Win) Res.drawable.win_icon
                else Res.drawable.lose_icon
            ),
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = if (gameResult == GameResult.Win) stringResource(Res.string.win)
            else stringResource(Res.string.lose),
            fontSize = 35.sp,
            fontWeight = FontWeight.SemiBold,
            color = ColorOnBackground,
            fontFamily = FontFamily(Font(Res.font.geologica_semibold))
        )
        Spacer(modifier = Modifier.height(20.dp))
        WordContainer(
            modifier = Modifier.fillMaxWidth(),
            wordLetters = generateWordLetterUI(
                word = actualWord,
                allCorrect = gameResult == GameResult.Win
            ),
            style = WordStyleSmall
        )
        Spacer(Modifier.height(45.dp))
        GameConditionContainer(
            modifier = Modifier.fillMaxWidth(),
            gameCondition = gameCondition,
            language = gameSettings.selectedLanguage,
            titleSize = 12.sp,
            contentSize = 26.sp,
            spaceBetween = 30.dp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Divider(
            modifier = Modifier.fillMaxWidth(), color = ColorOnBackground,
            thickness = 1.dp, startIndent = 1.dp
        )
        Spacer(Modifier.height(45.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            CustomButton(
                modifier = modifier.fillMaxWidth().weight(1f),
                text = stringResource(Res.string.share),
                onClick = share,
                fontColor = ColorOnBackground,
                icon = Icons.Outlined.Share,
            )
            Spacer(Modifier.width(10.dp))
            CustomButton(
                modifier = modifier.fillMaxWidth().weight(1f),
                text = stringResource(Res.string.play_again),
                onClick = playAgain
            )
        }
    }
}
