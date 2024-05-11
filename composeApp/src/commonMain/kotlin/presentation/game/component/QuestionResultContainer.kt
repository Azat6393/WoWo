package presentation.game.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.ui.ColorBackground
import component.ui.ColorGreen
import component.ui.ColorOnBackground
import component.ui.ColorRed
import component.ui.ColorYellow
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.stringResource
import presentation.game.AiResult
import presentation.game.Difficulty
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_medium
import wowo.composeapp.generated.resources.invalid
import wowo.composeapp.generated.resources.no
import wowo.composeapp.generated.resources.yes

@Composable
fun QuestionResultContainer(
    modifier: Modifier,
    aiResult: AiResult,
    aiResultForEasyMode: String = "",
    isEasyMode: Boolean,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (isEasyMode) {
            Text(
                text = aiResultForEasyMode,
                color = ColorOnBackground,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp)
            )
        } else {
            QuestionResultButton(
                modifier = Modifier.width(100.dp),
                text = stringResource(Res.string.yes),
                isSelected = aiResult == AiResult.Yes,
                backgroundColor = ColorGreen
            )
            Spacer(modifier = Modifier.width(5.dp))
            QuestionResultButton(
                modifier = Modifier.width(100.dp),
                text = stringResource(Res.string.no),
                isSelected = aiResult == AiResult.No,
                backgroundColor = ColorRed
            )
            Spacer(modifier = Modifier.width(5.dp))
            QuestionResultButton(
                modifier = Modifier.width(100.dp),
                text = stringResource(Res.string.invalid),
                isSelected = aiResult == AiResult.Invalid,
                backgroundColor = ColorYellow
            )
        }
    }
}

@Composable
private fun QuestionResultButton(
    modifier: Modifier,
    text: String,
    isSelected: Boolean,
    backgroundColor: Color,
) {

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(if (isSelected) backgroundColor else ColorBackground)
            .border(border = BorderStroke(1.dp, ColorOnBackground), RoundedCornerShape(5.dp))
            .padding(top = 4.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = ColorOnBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(Res.font.geologica_medium))
        )
    }
}