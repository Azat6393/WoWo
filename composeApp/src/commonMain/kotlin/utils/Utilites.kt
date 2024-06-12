package utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.currentKoinScope
import presentation.game.LetterCondition
import presentation.game.WordLetterUI

@Composable
fun Dp.dpToPx() = with(LocalDensity.current) { this@dpToPx.toPx() }


@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun generateWordLetterUI(word: String, allCorrect: Boolean): List<WordLetterUI> {
    val wordLetters = mutableStateListOf<WordLetterUI>()
    word.forEachIndexed { index, c ->
        wordLetters.add(
            WordLetterUI(
                index = index,
                letter = c.toString(),
                condition = when {
                    c == ' ' -> LetterCondition.Space
                    allCorrect -> LetterCondition.InCorrectSpot
                    else -> LetterCondition.NotInWord
                }
            )
        )
    }
    return wordLetters
}

fun generateWelcomeWordLetterUI(): List<WordLetterUI> {
    val wordLetters = mutableStateListOf<WordLetterUI>()
    "welcome".forEachIndexed { index, c ->
        wordLetters.add(
            WordLetterUI(
                index = index,
                letter = c.toString(),
                condition = when (c) {
                    'w', 'l', 'c' -> LetterCondition.InCorrectSpot
                    'e' -> LetterCondition.WrongSpot
                    else -> LetterCondition.NotInWord
                }
            )
        )
    }
    return wordLetters
}

@Composable
inline fun <reified T: ViewModel> koinViewModel(): T {
    val scope = currentKoinScope()
    return viewModel {
        scope.get<T>()
    }
}