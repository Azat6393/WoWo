package presentation.game

import domain.model.Category

sealed class GameEvent {
    data class OnInputLetter(val letter: String) : GameEvent()
    data class OnQuestionInput(val text: String) : GameEvent()
    data class OnLanguageChange(val language: String) : GameEvent()
    data class OnCategorySelect(val category: Category) : GameEvent()
    data class OnDifficultyChange(val difficulty: Difficulty) : GameEvent()
    data object OnDeleteLetter : GameEvent()
    data object Enter : GameEvent()
    data object AskQuestion : GameEvent()
    data object StartGame : GameEvent()
    data object GiveUp : GameEvent()
}