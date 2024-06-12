package presentation.game.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import presentation.component.CustomDialogLayout
import presentation.component.SettingContainer
import presentation.component.WordContainer
import presentation.component.WordStyleSmall
import presentation.component.ui.ColorBackground
import presentation.component.ui.ColorOnBackground
import presentation.component.ui.ColorPrimary
import domain.model.Category
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import presentation.game.Difficulty
import presentation.game.GameSettings
import utils.generateWelcomeWordLetterUI
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.how_to_play
import wowo.composeapp.generated.resources.play


@Composable
fun StartGameContainer(
    modifier: Modifier = Modifier,
    onStart: () -> Unit,
    gameSettings: GameSettings,
    onLanguageSelect: (String) -> Unit,
    onDifficultySelect: (Difficulty) -> Unit,
    onCategorySelect: (Category) -> Unit,
    howToPlay: () -> Unit,
) {

    CustomDialogLayout(modifier = modifier) {
        WordContainer(
            modifier = Modifier.fillMaxWidth(),
            wordLetters = generateWelcomeWordLetterUI(),
            style = WordStyleSmall
        )
        Spacer(Modifier.height(45.dp))
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
        Row(modifier = Modifier.fillMaxWidth()) {
            CustomButton(
                modifier = modifier.fillMaxWidth().weight(1f),
                text = stringResource(Res.string.how_to_play),
                onClick = howToPlay,
                fontColor = ColorOnBackground,
                icon = Icons.Outlined.Info,
            )
            Spacer(Modifier.width(10.dp))
            CustomButton(
                modifier = modifier.fillMaxWidth().weight(1f),
                text = stringResource(Res.string.play),
                onClick = onStart
            )
        }
    }
}

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    text: String,
    fontColor: Color = ColorPrimary,
    onClick: () -> Unit,
    icon: ImageVector? = null,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = ColorBackground,
            contentColor = fontColor
        ),
        border = BorderStroke(width = 1.dp, ColorOnBackground),
        contentPadding = PaddingValues(vertical = 12.dp),
        content = {
            Text(
                text = text,
                fontFamily = FontFamily(Font(Res.font.geologica_regular))
            )
            if (icon != null) {
                Spacer(Modifier.width(5.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        modifier = modifier
    )
}
