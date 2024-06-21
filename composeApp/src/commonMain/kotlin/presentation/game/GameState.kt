package presentation.game

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import presentation.component.MessageBarState
import domain.model.Category
import presentation.game.LetterCondition.Blank


data class GameState(
    val gameSettings: GameSettings = GameSettings(),
    val actualWord: String = "",
    val word: SnapshotStateList<WordLetterUI> = emptyLetters(),
    val notInWordLetters: SnapshotStateList<String> = mutableStateListOf(),
    val gameConditionsUI: GameConditionsUI = GameConditionsUI(0, 0, 0, 0),
    val question: String = "",
    val aiResult: AiResult = AiResult.Empty,
    val aiResultForEasyMode: String = "",
    val aiLoading: Boolean = false,
    val loading: Boolean = false,
    val isGameStarted: Boolean = false,
    val isEnterEnable: Boolean = false,
    val isQuestionEnable: Boolean = false,
    val isRewardedAdReady: Boolean = false,
    val message: MessageBarState = MessageBarState(),
    val gameResult: GameResult? = null,
    val showProfile: Boolean = false,
    val focusedCompose: FocusedCompose = FocusedCompose.Word
)

sealed class FocusedCompose {
    data object Question: FocusedCompose()
    data object Word: FocusedCompose()
}

data class GameConditionsUI(
    val question: Int = 0,
    val maxQuestion: Int = 0,
    val attempts: Int = 0,
    val maxAttempts: Int = 0,
)

data class WordLetterUI(
    val index: Int,
    val letter: String,
    val condition: LetterCondition,
)

data class GameSettings(
    val selectedLanguage: String = "eng",
    val selectedCategory: Category? = null,
    val languages: List<String> = listOf("eng", "tr", "ru"),
    val categories: List<Category> = emptyList(),
    val difficulty: Difficulty = Difficulty.Easy,
)

enum class Difficulty {
    Hard, Medium, Easy;

    fun getDifficulty(): Int {
        return when (this) {
            Hard -> 3
            Medium -> 2
            Easy -> 1
        }
    }
}

sealed class GameResult(val score: Int?) {
    data class Win(val earnedScore: Int? = null) : GameResult(score = earnedScore)
    data class Lose(val lostScore: Int? = null) : GameResult(score = lostScore)
}

enum class AiResult {
    Yes, No, Invalid, Empty;
}

enum class LetterCondition {
    NotInWord, WrongSpot, InCorrectSpot, Input, Blank, Space
}

private fun emptyLetters() = mutableStateListOf(
    WordLetterUI(
        index = 0,
        letter = " ",
        condition = Blank
    ),
    WordLetterUI(
        index = 1,
        letter = " ",
        condition = Blank
    ),
    WordLetterUI(
        index = 2,
        letter = " ",
        condition = Blank
    ),
    WordLetterUI(
        index = 3,
        letter = " ",
        condition = Blank
    ),
    WordLetterUI(
        index = 4,
        letter = " ",
        condition = Blank
    ),
    WordLetterUI(
        index = 5,
        letter = " ",
        condition = Blank
    ),
)