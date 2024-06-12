package presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import data.remote.body.InputWordBody
import data.remote.body.QuestionBody
import data.remote.body.ResultGameBody
import domain.model.Category
import domain.model.GameCondition
import domain.model.InputResult
import domain.model.QuestionEasyModeResult
import domain.model.QuestionResult
import domain.model.User
import domain.model.Word
import domain.repository.GameRepository
import domain.repository.UserRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import presentation.component.MessageBarState
import utils.UniqueIdGenerator
import wowo.composeapp.generated.resources.Res
import wowo.composeapp.generated.resources.choose_category
import wowo.composeapp.generated.resources.no_internet
import kotlin.random.Random

class GameViewModel(
    private val gameRepository: GameRepository,
    private val uniqueIdGenerator: UniqueIdGenerator,
    userRepository: UserRepository,
) : ViewModel() {

    var state by mutableStateOf(GameState())
        private set

    private val userId: String by lazy { uniqueIdGenerator.getId() }

    var showedAds = 0

    init {
        userRepository
            .getUser(userId)
            .onEach(::processUserResult)
            .catch { showErrorMessage(it) }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.OnInputLetter -> onInputLetter(event.letter)
            is GameEvent.OnDeleteLetter -> onDeleteLetter()
            GameEvent.Enter -> onEnter()
            GameEvent.AskQuestion -> askQuestion()
            GameEvent.StartGame -> startGame()
            is GameEvent.OnQuestionInput -> inputQuestionText(event.text)
            is GameEvent.OnCategorySelect -> onCategorySelect(event.category)
            is GameEvent.OnDifficultyChange -> onDifficultyChange(event.difficulty)
            is GameEvent.OnLanguageChange -> onLanguageChange(event.language)
            GameEvent.GiveUp -> giveUp()
            GameEvent.GetTips -> getTips()
            is GameEvent.OnRewardedAdStateChange -> state =
                state.copy(isRewardedAdReady = event.isLoaded)
        }
    }

    private fun processUserResult(result: Result<User>) {
        result.fold(
            onSuccess = { user ->
                state = state.copy(showProfile = user.uuid != "guest_user")
            },
            onFailure = {}
        )
    }

    private fun getTips() {
        val enabledIndexes = state.word.filter { word ->
            word.condition != LetterCondition.InCorrectSpot && word.condition != LetterCondition.Space
        }
        if (enabledIndexes.isEmpty()) return

        val index = if (enabledIndexes.lastIndex == 0) enabledIndexes[0].index
        else enabledIndexes[Random.nextInt(0, enabledIndexes.lastIndex)].index
        state.word[index] = state.word[index].copy(
            letter = state.actualWord[index].toString(),
            condition = LetterCondition.InCorrectSpot
        )
        checkEnterEnable()
    }

    private fun giveUp() {
        sendGameResult(false)
        state = state.copy(
            loading = false,
            gameResult = GameResult.Lose()
        )
    }

    private fun onLanguageChange(language: String) {
        val lan = if (!state.gameSettings.languages.contains(language)) "eng" else language
        getCategories(lan)
        state = state.copy(
            gameSettings = state.gameSettings.copy(selectedLanguage = lan)
        )
    }

    private fun onDifficultyChange(difficulty: Difficulty) {
        state = state.copy(
            gameSettings = state.gameSettings.copy(difficulty = difficulty)
        )
    }

    private fun onCategorySelect(category: Category) {
        state = state.copy(
            gameSettings = state.gameSettings.copy(
                selectedCategory = category
            )
        )
    }

    private fun getCategories(language: String) {
        gameRepository.getCategories(language)
            .onStart { state = state.copy(loading = true) }
            .onEach { result ->
                result.fold(
                    onSuccess = {
                        state = state.copy(
                            loading = false,
                            gameSettings = state.gameSettings.copy(categories = it)
                        )
                    },
                    onFailure = ::showErrorMessage
                )
            }
            .catch { showErrorMessage(it) }
            .launchIn(viewModelScope)
    }

    private fun onInputLetter(letter: String) {
        if (letter.isBlank()) return
        var index = 0
        while (index <= state.word.lastIndex) {
            if (state.word[index].condition == LetterCondition.Blank) {
                state.word[index] = state.word[index].copy(
                    letter = letter,
                    condition = LetterCondition.Input
                )
                break
            }
            index++
        }
        checkEnterEnable()
    }

    private fun onDeleteLetter() {
        var lastIndex = state.word.lastIndex
        while (lastIndex >= 0) {
            if (state.word[lastIndex].condition != LetterCondition.InCorrectSpot &&
                state.word[lastIndex].condition != LetterCondition.Blank &&
                state.word[lastIndex].condition != LetterCondition.Space
            ) {
                state.word[lastIndex] =
                    state.word[lastIndex].copy(condition = LetterCondition.Blank)
                break
            }
            lastIndex--
        }
        checkEnterEnable()
    }

    private fun onEnter() {
        var enteredWord = ""
        state.word.forEach {
            enteredWord = "$enteredWord${it.letter}"
        }
        val body = InputWordBody(
            actualWord = state.actualWord.replace("İ", "i").lowercase(),
            enteredWord = enteredWord.replace("İ", "i").lowercase(),
            userId = userId,
            difficultyLevel = state.gameSettings.difficulty.getDifficulty(),
            gameCondition = GameCondition(
                question = state.gameConditionsUI.question,
                seconds = 0,
                attempts = state.gameConditionsUI.attempts
            )
        )
        gameRepository.inputWord(
            inputWordBody = body
        )
            .onStart { state = state.copy(loading = true) }
            .onEach(::processInputResult)
            .catch { showErrorMessage(it) }.launchIn(viewModelScope)
    }

    private fun askQuestion() {
        if (state.gameSettings.difficulty == Difficulty.Easy) {
            askQuestionForEasyMode()
            return
        }
        gameRepository.askQuestion(
            questionBody = QuestionBody(
                word = state.actualWord,
                category = state.gameSettings.selectedCategory!!.name,
                question = if (state.question.last() != '?') "${state.question}?" else state.question,
                language = state.gameSettings.selectedLanguage
            )
        )
            .onStart { state = state.copy(aiLoading = true) }
            .onEach(::processAiResult)
            .catch { showErrorMessage(it) }.launchIn(viewModelScope)
    }

    private fun askQuestionForEasyMode() {
        gameRepository.askQuestionForEasyMode(
            questionBody = QuestionBody(
                word = state.actualWord,
                category = state.gameSettings.selectedCategory!!.name,
                question = state.question,
                language = state.gameSettings.selectedLanguage
            )
        )
            .onStart { state = state.copy(aiLoading = true) }
            .onEach(::processAiResultForEasyMode)
            .catch { showErrorMessage(it) }.launchIn(viewModelScope)
    }

    private fun startGame() = viewModelScope.launch {
        if (state.gameSettings.selectedCategory == null) {
            state = state.copy(
                message = MessageBarState(
                    uuid = "Category error",
                    error = getString(Res.string.choose_category)
                )
            )
            return@launch
        }
        gameRepository.getWord(
            category = state.gameSettings.selectedCategory!!.uuid,
            language = state.gameSettings.selectedLanguage,
            difficulty = state.gameSettings.difficulty.getDifficulty()
        )
            .onStart { state = state.copy(loading = true) }
            .onEach(::processWordResult)
            .catch { showErrorMessage(it) }
            .launchIn(viewModelScope)
    }


    private fun inputQuestionText(text: String) {
        if (text.length > 40) return
        state = state.copy(
            question = text
        )
        checkQuestionEnable()
    }

    private fun processWordResult(result: Result<Word>) {
        result.fold(
            onSuccess = { word ->
                state = state.copy(
                    actualWord = word.word,
                    word = generateBlackWordLetterUI(word.word),
                    gameConditionsUI = state.gameConditionsUI.copy(
                        maxQuestion = word.gameCondition.question,
                        maxAttempts = word.gameCondition.attempts,
                        question = 0,
                        attempts = 0
                    ),
                    loading = false,
                    isGameStarted = true,
                    gameResult = null,
                    question = "",
                    aiResultForEasyMode = "",
                    aiResult = AiResult.Empty,
                    notInWordLetters = mutableStateListOf()
                )
            },
            onFailure = ::showErrorMessage
        )
    }

    private suspend fun processAiResultForEasyMode(result: Result<QuestionEasyModeResult>) {
        result.fold(
            onSuccess = { questionResult ->
                state = state.copy(
                    aiLoading = false,
                    aiResultForEasyMode = questionResult.answer,
                    gameConditionsUI = state.gameConditionsUI.copy(
                        question = state.gameConditionsUI.question + 1
                    )
                )
            },
            onFailure = { exception ->
                val message = if (
                    exception.message?.startsWith("Unable") == true
                ) getString(Res.string.no_internet) else exception.message
                state = state.copy(
                    aiLoading = false,
                    message = MessageBarState(
                        uuid = exception.hashCode().toString(),
                        message = null,
                        error = message
                    )
                )
            }
        )
    }

    private fun processAiResult(result: Result<QuestionResult>) {
        result.fold(
            onSuccess = { questionResult ->
                state = when (questionResult.answer) {
                    1 -> state.copy(
                        aiLoading = false,
                        aiResult = AiResult.Yes,
                        gameConditionsUI = state.gameConditionsUI.copy(
                            question = state.gameConditionsUI.question + 1
                        )
                    )

                    2 -> state.copy(
                        aiLoading = false,
                        aiResult = AiResult.No,
                        gameConditionsUI = state.gameConditionsUI.copy(
                            question = state.gameConditionsUI.question + 1
                        )
                    )

                    else -> state.copy(
                        aiLoading = false,
                        aiResult = AiResult.Invalid,
                        gameConditionsUI = state.gameConditionsUI.copy(
                            question = state.gameConditionsUI.question + 1
                        )
                    )
                }
                checkQuestionEnable()
            },
            onFailure = ::showErrorMessage
        )
    }

    private fun processInputResult(result: Result<InputResult>) {
        result.fold(
            onSuccess = { inputResult ->
                state.word.forEachIndexed { index, wordLetter ->
                    when (inputResult.lettersCondition[index]) {
                        0 -> {
                            if (!state.notInWordLetters.contains(wordLetter.letter)) {
                                state.notInWordLetters.add(wordLetter.letter)
                            }
                            state.word[index] =
                                state.word[index].copy(condition = LetterCondition.NotInWord)
                        }

                        1 -> state.word[index] =
                            state.word[index].copy(condition = LetterCondition.WrongSpot)

                        2 -> state.word[index] =
                            state.word[index].copy(condition = LetterCondition.InCorrectSpot)

                        3 -> state.word[index] =
                            state.word[index].copy(condition = LetterCondition.Space)
                    }
                }

                state = if (inputResult.isCorrect) {
                    sendGameResult(true)
                    state.copy(
                        loading = false,
                        gameResult = GameResult.Win(inputResult.earnedScore)
                    )
                } else {
                    if (state.gameConditionsUI.attempts + 1
                        == state.gameConditionsUI.maxAttempts
                    ) sendGameResult(false)

                    state.copy(
                        loading = false,
                        gameResult = if (state.gameConditionsUI.attempts + 1
                            == state.gameConditionsUI.maxAttempts
                        ) GameResult.Lose() else null,
                        gameConditionsUI = state.gameConditionsUI.copy(
                            attempts = state.gameConditionsUI.attempts + 1
                        )
                    )
                }
            },
            onFailure = ::showErrorMessage
        )
    }

    private fun generateBlackWordLetterUI(word: String): SnapshotStateList<WordLetterUI> {
        val wordLetters = mutableStateListOf<WordLetterUI>()
        word.forEachIndexed { index, c ->
            wordLetters.add(
                WordLetterUI(
                    index = index,
                    letter = c.toString(),
                    condition = if (c == ' ') LetterCondition.Space else LetterCondition.Blank
                )
            )
        }
        return wordLetters
    }

    private fun checkQuestionEnable() {
        state = state.copy(
            isQuestionEnable = state.gameConditionsUI.maxQuestion > state.gameConditionsUI.question && state.question.isNotBlank()
        )
    }

    private fun checkEnterEnable() {
        state = state.copy(
            isEnterEnable = state.word.find { it.condition == LetterCondition.Blank } == null
        )
    }

    private fun sendGameResult(isWin: Boolean) {
        showedAds = 0
        viewModelScope.launch {
            gameRepository.gameResult(
                resultGameBody = ResultGameBody(
                    win = isWin,
                    actualWord = state.actualWord,
                    userId = userId,
                    difficultyLevel = state.gameSettings.difficulty.getDifficulty(),
                    gameCondition = GameCondition(
                        question = state.gameConditionsUI.question,
                        seconds = 0,
                        attempts = state.gameConditionsUI.attempts
                    )
                )
            )
        }
    }

    private fun showErrorMessage(exception: Throwable) = viewModelScope.launch {
        val message = if (
            exception.message?.startsWith("Unable") == true
        ) getString(Res.string.no_internet) else exception.message
        state = state.copy(
            loading = false,
            message = MessageBarState(
                uuid = exception.hashCode().toString(),
                message = null,
                error = message
            )
        )
    }
}