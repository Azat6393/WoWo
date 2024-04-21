package com.caelum.wowo.service

import com.caelum.wowo.models.body.InputWordBody
import com.caelum.wowo.models.body.QuestionBody
import com.caelum.wowo.models.response.InputResponse
import com.caelum.wowo.models.response.QuestionResponse
import com.caelum.wowo.models.toWord
import com.caelum.wowo.models.wowo.Word
import com.caelum.wowo.repository.GptRepository
import com.caelum.wowo.repository.UserRepository
import com.caelum.wowo.repository.WordRepository
import com.caelum.wowo.utils.calculateScore
import com.caelum.wowo.utils.changeLetterCondition
import com.caelum.wowo.utils.exception.UnknownException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GameService(
    private val wordRepository: WordRepository,
    private val userRepository: UserRepository,
    private val gptRepository: GptRepository,
) {

    fun getRandomWord(language: String, category: String): Flow<Word> = flow {
        val result = wordRepository.getRandomWord(language, category)
        result.fold(
            onSuccess = { wordDto ->
                emit(wordDto.toWord())
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }

    fun askQuestion(questionBody: QuestionBody): Flow<QuestionResponse> = flow {
        val result = gptRepository.sendMessage(
            word = questionBody.word,
            question = questionBody.question,
            language = questionBody.language
        )
        result.fold(
            onSuccess = { message ->
                val answer = message.content.lowercase()
                when {
                    answer.startsWith("yes") -> emit(QuestionResponse(1))
                    answer.startsWith("no") -> emit(QuestionResponse(2))
                    else -> emit(QuestionResponse(3))
                }
            },
            onFailure = { exception: Throwable ->
                throw exception
            }
        )
    }

    fun inputWord(inputWordBody: InputWordBody): Flow<InputResponse> = flow {
        val actualWord = inputWordBody.actualWord
        val enteredWord = inputWordBody.enteredWord
        if (actualWord == enteredWord) {
            val earnedScore = calculateScore(
                seconds = inputWordBody.seconds,
                attempts = inputWordBody.attempts,
                questions = inputWordBody.question
            )
            userRepository.increaseScore(
                score = earnedScore,
                userId = inputWordBody.userId
            )
            val inputResponse = InputResponse(
                isCorrect = true,
                lettersCondition = changeLetterCondition(
                    enteredWord = enteredWord,
                    actualWord = actualWord
                ),
                earnedScore = earnedScore
            )
            emit(inputResponse)
        } else {
            val inputResponse = InputResponse(
                isCorrect = false,
                lettersCondition = changeLetterCondition(
                    enteredWord = enteredWord,
                    actualWord = actualWord
                ),
                earnedScore = null
            )
            emit(inputResponse)
        }
    }
}