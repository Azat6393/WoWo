package component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.ui.ColorBackground
import component.ui.ColorGray
import component.ui.ColorGreen
import component.ui.ColorOnBackground
import component.ui.ColorRed
import component.ui.ColorYellow
import presentation.game.LetterCondition
import presentation.game.WordLetterUI

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WordContainer(
    modifier: Modifier,
    wordLetters: List<WordLetterUI>,
) {

    FlowRow(modifier = modifier, horizontalArrangement = Arrangement.Center) {
        wordLetters.forEach { letter ->
            Spacer(modifier = Modifier.width(5.dp))
            WordItem(modifier = Modifier.padding(bottom = 5.dp).size(45.dp), letter = letter)
        }
    }
}

@Composable
fun WordItem(
    modifier: Modifier,
    letter: WordLetterUI,
) {

    var backgroundColor by remember {
        mutableStateOf(ColorBackground)
    }

    LaunchedEffect(letter) {
        backgroundColor =  when (letter.condition) {
            LetterCondition.NotInWord -> ColorRed
            LetterCondition.WrongSpot -> ColorYellow
            LetterCondition.InCorrectSpot -> ColorGreen
            LetterCondition.Input -> ColorBackground
            LetterCondition.Blank -> ColorBackground
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(5.dp))
            .background(backgroundColor)
            .border(
                border = BorderStroke(
                    if (letter.condition == LetterCondition.Blank) 1.dp else 1.5.dp,
                    if (letter.condition == LetterCondition.Blank) ColorGray
                    else Color.Black
                ),
                shape = RoundedCornerShape(5.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        if (letter.condition != LetterCondition.Blank) {
            Text(
                text = letter.letter.uppercase(),
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}