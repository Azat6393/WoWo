package component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.ui.ColorBackground
import component.ui.ColorGreen
import component.ui.ColorOnBackground
import component.ui.ColorRed
import component.ui.ColorYellow
import presentation.Strings
import presentation.game.AiResult

@Composable
fun QuestionResultContainer(
    modifier: Modifier,
    aiResult: AiResult,
    isWeb: Boolean,
    language: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        QuestionResultButton(
            modifier = Modifier.width(if (isWeb) 130.dp else 100.dp),
            text = Strings.yes(language),
            isSelected = aiResult == AiResult.Yes,
            backgroundColor = ColorGreen
        )
        Spacer(modifier = Modifier.width(5.dp))
        QuestionResultButton(
            modifier = Modifier.width(if (isWeb) 130.dp else 100.dp),
            text = Strings.noText(language),
            isSelected = aiResult == AiResult.No,
            backgroundColor = ColorRed
        )
        Spacer(modifier = Modifier.width(5.dp))
        QuestionResultButton(
            modifier = Modifier.width(if (isWeb) 130.dp else 100.dp),
            text = Strings.invalidText(language),
            isSelected = aiResult == AiResult.Invalid,
            backgroundColor = ColorYellow
        )
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
            fontWeight = FontWeight.Medium
        )
    }
}