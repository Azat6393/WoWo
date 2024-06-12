package presentation.profile

import presentation.component.MessageBarState
import domain.model.Statistics


data class GameState(
    val totalWins: Int = 0,
    val totalLoses: Int = 0,
    val totalPlayed: Int = 0,
    val questionsPerGame: Int = 0,
    val attemptsPerGame: Int = 0,

    val easyStatistics: Statistics = Statistics(),
    val mediumStatistics: Statistics = Statistics(),
    val hardStatistics: Statistics = Statistics(),

    val nickname: String = "",
    val score: Int = 0,
    val email: String? = null,
    val userLoading: Boolean = false,
    val statisticsLoading: Boolean = false,
    val message: MessageBarState = MessageBarState(),
)
