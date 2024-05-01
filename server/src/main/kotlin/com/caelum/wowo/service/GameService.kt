package com.caelum.wowo.service

import com.caelum.wowo.models.body.InputWordBody
import com.caelum.wowo.models.body.QuestionBody
import com.caelum.wowo.models.toCategory
import com.caelum.wowo.models.toWord
import com.caelum.wowo.models.wowo.Category
import com.caelum.wowo.models.wowo.InputResult
import com.caelum.wowo.models.wowo.QuestionResult
import com.caelum.wowo.models.wowo.Word
import com.caelum.wowo.repository.CategoryRepository
import com.caelum.wowo.repository.GptRepository
import com.caelum.wowo.repository.UserRepository
import com.caelum.wowo.repository.WordRepository
import com.caelum.wowo.utils.GUEST_USER
import com.caelum.wowo.utils.calculateScore
import com.caelum.wowo.utils.changeLetterCondition
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameService(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository,
    private val gptRepository: GptRepository,
    private val categoryRepository: CategoryRepository,
) {

    suspend fun getRandomWord(language: String, category: String, difficulty: Int): Flow<Word> =
        flow {
            val result = wordRepository.getRandomWord(language, category)
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
            language = questionBody.language
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

    fun inputWord(inputWordBody: InputWordBody): Flow<InputResult> = flow {
        val actualWord = inputWordBody.actualWord
        val enteredWord = inputWordBody.enteredWord
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