package presentation.game

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import component.MessageBarState
import data.remote.body.InputWordBody
import data.remote.body.QuestionBody
import domain.model.Category
import domain.model.GameCondition
import domain.model.InputResult
import domain.model.QuestionResult
import domain.model.Word
import domain.repository.GameRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {

    var state by mutableStateOf(GameState())
        private set

    init {
        getCategories(state.gameSettings.selectedLanguage)
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
        }
    }

    private fun onLanguageChange(language: String) {
        getCategories(language)
        state = state.copy(
            gameSettings = state.gameSettings.copy(selectedLanguage = language)
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
        state = state.copy(loading = true)
        gameRepository.getCategories(language).onEach { result ->
            result.fold(
                onSuccess = {
                    state = state.copy(
                        loading = false,
                        gameSettings = state.gameSettings.copy(categories = it)
                    )
                },
                onFailure = { exception ->
                    state = state.copy(
                        loading = false,
                        message = MessageBarState(
                            uuid = exception.hashCode().toString(),
                            message = null,
                            error = exception.message
                        )
                    )
                }
            )
        }.launchIn(viewModelScope)
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
                state.word[lastIndex].condition != LetterCondition.Blank
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
        state = state.copy(loading = true)
        val entered = state.word.joinToString { it.letter }
        gameRepository.inputWord(
            inputWordBody = InputWordBody(
                actualWord = state.actualWord,
                enteredWord = entered,
                userId = "guest_user",
                difficultyLevel = state.gameSettings.difficulty.getDifficulty(),
                gameCondition = GameCondition(
                    question = state.gameConditionsUI.question,
                    seconds = 0,
                    attempts = state.gameConditionsUI.attempts
                )
            )
        ).onEach(::processInputResult).launchIn(viewModelScope)
    }

    private fun askQuestion() {
        state = state.copy(aiLoading = true)
        gameRepository.askQuestion(
            questionBody = QuestionBody(
                word = state.actualWord,
                question = state.question,
                language = state.gameSettings.selectedLanguage
            )
        ).onEach(::processAiResult).launchIn(viewModelScope)
    }

    private fun startGame() = viewModelScope.launch {
        if (state.gameSettings.selectedCategory == null) {
            state = state.copy(
                message = MessageBarState(
                    uuid = "Category error",
                    error = "Please choose category"
                )
            )
            return@launch
        }
        state = state.copy(loading = true)
        gameRepository.getWord(
            category = state.gameSettings.selectedCategory!!.name,
            language = state.gameSettings.selectedLanguage,
            difficulty = state.gameSettings.difficulty.getDifficulty()
        ).onEach(::processWordResult).launchIn(viewModelScope)
    }


    private fun inputQuestionText(text: String) {
        if (state.question.length >= 20) return
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
                    aiResult = AiResult.Empty
                )
            },
            onFailure = { exception ->
                state = state.copy(
                    loading = false,
                    message = MessageBarState(
                        uuid = exception.hashCode().toString(),
                        message = null,
                        error = exception.message
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
            onFailure = { exception ->
                state = state.copy(
                    aiLoading = false,
                    message = MessageBarState(
                        uuid = exception.hashCode().toString(),
                        message = null,
                        error = exception.message
                    )
                )
            }
        )
    }

    private fun processInputResult(result: Result<InputResult>) {
        result.fold(
            onSuccess = { inputResult ->
                state.word.forEachIndexed { index, _ ->
                    when (inputResult.lettersCondition[index]) {
                        0 -> state.word[index] =
                            state.word[index].copy(condition = LetterCondition.NotInWord)

                        1 -> state.word[index] =
                            state.word[index].copy(condition = LetterCondition.WrongSpot)

                        2 -> state.word[index] =
                            state.word[index].copy(condition = LetterCondition.InCorrectSpot)
                    }
                }

                state = if (inputResult.isCorrect) {
                    state.copy(
                        loading = false,
                        gameResult = GameResult.Win
                    )
                } else {
                    state.copy(
                        loading = false,
                        gameResult = if (state.gameConditionsUI.attempts + 1
                            == state.gameConditionsUI.maxAttempts
                        ) GameResult.Lose else null,
                        gameConditionsUI = state.gameConditionsUI.copy(
                            attempts = state.gameConditionsUI.attempts + 1
                        )
                    )
                }
            },
            onFailure = { exception ->
                state = state.copy(
                    loading = false,
                    message = MessageBarState(
                        uuid = exception.hashCode().toString(),
                        message = null,
                        error = exception.message
                    )
                )
            }
        )
    }

    private fun generateBlackWordLetterUI(word: String): SnapshotStateList<WordLetterUI> {
        val wordLetters = mutableStateListOf<WordLetterUI>()
        word.forEachIndexed { index, c ->
            wordLetters.add(
                WordLetterUI(
                    index = index,
                    letter = c.toString(),
                    condition = LetterCondition.Blank
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
}