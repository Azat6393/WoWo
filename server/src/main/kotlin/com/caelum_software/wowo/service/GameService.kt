package com.caelum_software.wowo.service

import com.caelum_software.wowo.models.body.InputWordBody
import com.caelum_software.wowo.models.body.QuestionBody
import com.caelum_software.wowo.models.body.ResultGameBody
import com.caelum_software.wowo.models.toCategory
import com.caelum_software.wowo.models.toWord
import com.caelum_software.wowo.models.wowo.Category
import com.caelum_software.wowo.models.wowo.InputResult
import com.caelum_software.wowo.models.wowo.QuestionEasyModeResult
import com.caelum_software.wowo.models.wowo.QuestionResult
import com.caelum_software.wowo.models.wowo.Word
import com.caelum_software.wowo.repository.CategoryRepository
import com.caelum_software.wowo.repository.GptRepository
import com.caelum_software.wowo.repository.StatisticsRepository
import com.caelum_software.wowo.repository.UserRepository
import com.caelum_software.wowo.repository.WordRepository
import com.caelum_software.wowo.utils.GUEST_USER
import com.caelum_software.wowo.utils.calculateScore
import com.caelum_software.wowo.utils.changeLetterCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameService(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository,
    private val gptRepository: GptRepository,
    private val categoryRepository: CategoryRepository,
    private val statisticsRepository: StatisticsRepository,
) {

    suspend fun getRandomWord(language: String, category: String, difficulty: Int): Flow<Word> =
        flow {
            val result = wordRepository.getRandomWord(language, category, difficulty)
            result.fold(
                onSuccess = { wordDto ->
                    emit(wordDto.toWord(difficulty = difficulty))
                },
                onFailure = { exception: Throwable ->
                    throw exception
                }
            )
        }

    fun askQuestion(questionBody: QuestionBody): Flow<QuestionResult> = flow {
        val result = gptRepository.sendMessage(
            word = questionBody.word,
            question = questionBody.question,
            language = questionBody.language,
            category = questionBody.category
        )
        result.fold(
            onSuccess = { message ->
                val answer = message.content.lowercase()
                when {
                    answer.startsWith("yes") -> emit(QuestionResult(1))
                    answer.startsWith("no") -> emit(QuestionResult(2))
                    else -> emit(QuestionResult(3))
                }
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }

    fun askQuestionForAnswer(questionBody: QuestionBody): Flow<QuestionEasyModeResult> = flow {
        val result = gptRepository.sendMessageFowEasyMode(
            word = questionBody.word,
            question = questionBody.question,
            language = questionBody.language,
            category = questionBody.category
        )
        result.fold(
            onSuccess = { message ->
                val answer = message.content
                emit(QuestionEasyModeResult(answer))
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }

    fun getCategories(language: String): Flow<List<Category>> = flow {
        val result = categoryRepository.getCategories(language)
        result.fold(
            onSuccess = { categories ->
                emit(categories.map { it.toCategory() })
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }

    suspend fun gameResult(resultGameBody: ResultGameBody) {
        statisticsRepository.updateUserStatistics(
            userId = resultGameBody.userId,
            isWin = resultGameBody.win,
            gameCondition = resultGameBody.gameCondition,
            difficulty = resultGameBody.difficultyLevel
        )
    }

    fun inputWord(inputWordBody: InputWordBody): Flow<InputResult> = flow {
        val actualWord = inputWordBody.actualWord.lowercase()
        val enteredWord = inputWordBody.enteredWord.lowercase()
        if (actualWord == enteredWord) {
            val earnedScore = calculateScore(
                seconds = inputWordBody.gameCondition.seconds,
                attempts = inputWordBody.gameCondition.attempts,
                questions = inputWordBody.gameCondition.question,
                difficulty = inputWordBody.difficultyLevel
            )
            if (inputWordBody.userId != GUEST_USER) {
                userRepository.increaseScore(
                    score = earnedScore,
                    userId = inputWordBody.userId
                )
            }
            val inputResult = InputResult(
                isCorrect = true,
                lettersCondition = changeLetterCondition(
                    enteredWord = enteredWord,
                    actualWord = actualWord
                ),
                earnedScore = if (inputWordBody.userId != GUEST_USER) earnedScore else null
            )
            emit(inputResult)
        } else {
            val inputResult = InputResult(
                isCorrect = false,
                lettersCondition = changeLetterCondition(
                    enteredWord = enteredWord,
                    actualWord = actualWord
                ),
                earnedScore = null
            )
            emit(inputResult)
        }
    }
}