package com.caelum_software.wowo.utils

import com.caelum_software.wowo.models.wowo.GameCondition
import com.caelum_software.wowo.models.response.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<Unit, ApplicationCall>.fetchError(exception: Exception) {
    call.respond(
        HttpStatusCode.BadRequest,
        ErrorResponse(
            exception.message.toString(),
            HttpStatusCode.BadRequest.value
        )
    )
}

suspend fun PipelineContext<Unit, ApplicationCall>.responseError(text: String) {
    call.respond(
        HttpStatusCode.BadRequest,
        ErrorResponse(
            text,
            HttpStatusCode.BadRequest.value
        )
    )
}


fun calculateScore(seconds: Int, attempts: Int, questions: Int, difficulty: Int): Int {
    return ((12 - attempts) + (18 - questions) * difficulty)
}

fun getGameCondition(difficulty: Int): GameCondition {
    return when (difficulty) {
        2 -> GameCondition(
            question = 12,
            seconds = 200,
            attempts = 8
        )

        3 -> GameCondition(
            question = 8,
            seconds = 200,
            attempts = 4
        )

        else -> GameCondition(
            question = 8,
            seconds = 200,
            attempts = 7
        )
    }
}

fun changeLetterCondition(enteredWord: String, actualWord: String): Map<Int, Int> {
    val inputResult = mutableMapOf<Int, Int>()
    enteredWord.forEachIndexed { index, c ->
        inputResult[index] = when {
            c == ' ' -> 3
            enteredWord[index] == actualWord[index] -> 2
            actualWord.contains(c) -> 1
            else -> 0
        }
    }
    return inputResult
}