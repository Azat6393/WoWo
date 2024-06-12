package presentation.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import presentation.component.MessageBarState
import domain.model.User
import domain.model.UserStatistics
import domain.repository.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import utils.UniqueIdGenerator
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.no_internet

class ProfileViewModel(
    private val uniqueIdGenerator: UniqueIdGenerator,
    private val userRepository: UserRepository,
) : ViewModel() {

    var state by mutableStateOf(GameState())
        private set

    private val userId: String by lazy { uniqueIdGenerator.getId() }
    private var currentUser: User? = null

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.GetUserInfo -> getUser(userId)
            ProfileEvent.GetUserStatistics -> getStatistics(userId)
            ProfileEvent.SaveNickname -> saveNickname()
            is ProfileEvent.OnNickNameChanged -> onNicknameChanged(event.nickname)
        }
    }

    private fun saveNickname() {
        if (state.nickname != currentUser?.nickname) {
            userRepository.updateUser(
                user = User(
                    uuid = userId,
                    nickname = state.nickname,
                    total_score = state.score,
                    email = state.email
                )
            ).onEach { result ->
                result.fold(
                    onSuccess = (::processUserResult),
                    onFailure = (::showErrorMessage)
                )
            }
                .catch { showErrorMessage(it) }
                .launchIn(viewModelScope)
        }
    }

    private fun onNicknameChanged(nickname: String) {
        state = state.copy(nickname = nickname)
    }

    private fun getUser(userId: String) {
        userRepository.getUser(userId)
            .onStart { state = state.copy(userLoading = true) }
            .onEach { result ->
                result.fold(
                    onSuccess = (::processUserResult),
                    onFailure = (::showErrorMessage)
                )
            }
            .catch { showErrorMessage(it) }
            .launchIn(viewModelScope)
    }

    private fun getStatistics(userId: String) {
        userRepository.getUserStatistics(userId)
            .onStart { state = state.copy(statisticsLoading = true) }
            .onEach { result ->
                result.fold(
                    onSuccess = (::processUserStatisticsResult),
                    onFailure = (::showErrorMessage)
                )
            }
            .catch { showErrorMessage(it) }
            .launchIn(viewModelScope)
    }

    private fun processUserStatisticsResult(userStatistics: UserStatistics) {
        val totalPlayed = userStatistics.easy.played +
                userStatistics.medium.played +
                userStatistics.hard.played
        val totalQuestions = userStatistics.easy.totalQuestion +
                userStatistics.medium.totalQuestion +
                userStatistics.hard.totalQuestion
        val totalAttempts = userStatistics.easy.totalAttempt +
                userStatistics.medium.totalAttempt +
                userStatistics.hard.totalAttempt
        state = state.copy(
            totalWins = userStatistics.easy.wins +
                    userStatistics.medium.wins +
                    userStatistics.hard.wins,
            totalLoses = userStatistics.easy.loses +
                    userStatistics.medium.loses +
                    userStatistics.hard.loses,
            totalPlayed = totalPlayed,
            questionsPerGame = if (totalQuestions != 0) totalQuestions / totalPlayed else 0,
            attemptsPerGame = if (totalAttempts != 0) totalAttempts / totalPlayed else 0,
            easyStatistics = userStatistics.easy,
            mediumStatistics = userStatistics.medium,
            hardStatistics = userStatistics.hard,
            statisticsLoading = false
        )
    }

    private fun processUserResult(user: User) {
        currentUser = user
        state = state.copy(
            userLoading = false,
            nickname = user.nickname,
            score = user.total_score
        )
    }

    private fun showErrorMessage(exception: Throwable) = viewModelScope.launch {
        val message = if (
            exception.message?.startsWith("Unable") == true
        ) getString(Res.string.no_internet) else exception.message
        state = state.copy(
            statisticsLoading = false,
            message = MessageBarState(
                uuid = exception.hashCode().toString(),
                message = null,
                error = message
            )
        )
    }
}