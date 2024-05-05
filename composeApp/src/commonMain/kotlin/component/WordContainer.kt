package component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.ui.ColorBackground
import component.ui.ColorGray
import component.ui.ColorGreen
import component.ui.ColorRed
import component.ui.ColorYellow
import org.jetbrains.compose.resources.Font
import presentation.game.LetterCondition
import presentation.game.WordLetterUI
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.geologica_extrabold

data class WordStyle(
    val spaceBetween: Dp = 5.dp,
    val size: Dp = 45.dp,
    val letterStyle: LetterStyle = LetterStyle(),
)

data class LetterStyle(
    val strokeWidth: Dp = 1.5.dp,
    val emptyLetterStrokeWidth: Dp = 1.dp,
    val corner: Dp = 5.dp,
    val lineSize: Dp = 2.dp,
    val fontSize: TextUnit = 18.sp,
)

val WordStyleSmall = WordStyle(
    spaceBetween = 4.dp,
    size = 35.dp,
    letterStyle = LetterStyle(
        corner = 4.dp,
        lineSize = 1.dp,
        fontSize = 14.sp
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordContainer(
    modifier: Modifier,
    wordLetters: List<WordLetterUI>,
    style: WordStyle = WordStyle(),
) {

    FlowRow(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        wordLetters.forEach { letter ->
            Spacer(modifier = Modifier.width(style.spaceBetween))
            WordItem(
                modifier = Modifier.padding(bottom = style.spaceBetween).size(style.size),
                letter = letter,
                style = style.letterStyle
            )
        }
    }
}

@Composable
fun WordItem(
    modifier: Modifier,
    letter: WordLetterUI,
    style: LetterStyle,
) {

    val color by animateColorAsState(
        targetValue = when (letter.condition) {
            LetterCondition.NotInWord -> ColorRed
            LetterCondition.WrongSpot -> ColorYellow
            LetterCondition.InCorrectSpot -> ColorGreen
            LetterCondition.Input -> ColorBackground
            LetterCondition.Blank -> ColorBackground
            LetterCondition.Space -> Color.Transparent
        },
        animationSpec = tween(300)
    )

    val borderWidth by animateDpAsState(
        if (letter.condition == LetterCondition.Blank) style.emptyLetterStrokeWidth
        else style.strokeWidth
    )
    val borderColor by animateColorAsState(
        if (letter.condition == LetterCondition.Blank) ColorGray else Color.Black
    )

    Box(
        modifier = modifier
            .then(
                if (letter.condition == LetterCondition.Space) modifier
                    .background(color)
                else modifier.clip(RoundedCornerShape(style.corner))
                    .background(color)
                    .border(
                        border = BorderStroke(
                            width = borderWidth,
                            color = borderColor
                        ),
                        shape = RoundedCornerShape(style.corner)
                    )
            )
    ) {
        if (letter.condition != LetterCondition.Blank && letter.condition != LetterCondition.Space) {
            Text(
                text = letter.letter.uppercase(),
                color = Color.Black,
                fontSize = style.fontSize,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily(Font(Res.font.geologica_extrabold)),
                modifier = Modifier.align(Alignment.Center)
            )
        }
        if (letter.condition == LetterCondition.Space) {
            Divider(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.Center),
                color = ColorGray,
                startIndent = style.lineSize,
                thickness = style.lineSize
            )
        }
    }
}