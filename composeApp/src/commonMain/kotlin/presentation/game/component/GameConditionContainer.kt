package presentation.game.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import presentation.component.ui.ColorPrimary
import presentation.component.ui.ColorSecondary
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import presentation.game.GameConditionsUI
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_medium
import wowo.composeapp.generated.resources.geologica_regular
import wowo.composeapp.generated.resources.incorrect
import wowo.composeapp.generated.resources.questions

@Composable
fun GameConditionContainer(
    modifier: Modifier,
    gameCondition: GameConditionsUI,
    language: String,
    titleSize: TextUnit = 14.sp,
    contentSize: TextUnit = 30.sp,
    spaceBetween: Dp = 80.dp
) {

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        GameConditionItem(
            title = stringResource(Res.string.incorrect),
            text = "${gameCondition.attempts}/${gameCondition.maxAttempts}",
            titleSize = titleSize,
            contentSize = contentSize
        )
        Spacer(Modifier.width(spaceBetween))
        GameConditionItem(
            title = stringResource(Res.string.questions),
            text = "${gameCondition.question}/${gameCondition.maxQuestion}",
            titleSize = titleSize,
            contentSize = contentSize
        )
    }
}

@Composable
fun GameConditionItem(
    title: String,
    text: String,
    titleSize: TextUnit = 14.sp,
    contentSize: TextUnit = 30.sp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            color = ColorSecondary,
            fontSize = titleSize,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(Res.font.geologica_medium))
        )
        Text(
            text = text,
            color = ColorPrimary,
            fontSize = contentSize,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily(Font(Res.font.geologica_regular))
        )
    }
}