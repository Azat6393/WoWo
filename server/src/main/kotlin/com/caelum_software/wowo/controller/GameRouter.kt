package com.caelum_software.wowo.controller

import com.caelum_software.wowo.models.body.InputWordBody
import com.caelum_software.wowo.models.body.QuestionBody
import com.caelum_software.wowo.models.body.ResultGameBody
import com.caelum_software.wowo.models.response.SuccessResponse
import com.caelum_software.wowo.service.GameService
import com.caelum_software.wowo.utils.ApiPaths
import com.caelum_software.wowo.utils.fetchError
import com.caelum_software.wowo.utils.responseError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.firstOrNull
import org.koin.ktor.ext.inject

fun Route.gameRouter() {
    val gameService by inject<GameService>()

    route(ApiPaths.GAME) {

        get("/word/{category?}/{language?}/{difficulty?}") {
            try {
                val language =
                    call.parameters["language"] ?: return@get responseError("Missing language")
                val category =
                    call.parameters["category"] ?: return@get responseError("Missing category")
                val difficulty =
                    call.parameters["difficulty"] ?: return@get responseError("Missing difficulty")
                val result = gameService.getRandomWord(
                    language = language,
                    category = category,
                    difficulty = difficulty.toInt()
                ).firstOrNull()

                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        result,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (e: Exception) {
                fetchError(e)
            }
        }

        get("/categories/{language?}") {
            try {
                val language =
                    call.parameters["language"] ?: return@get responseError("Missing language")
                val result = gameService.getCategories(language).firstOrNull()
                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        result,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (e: Exception) {
                fetchError(e)
            }
        }

        post<InputWordBody>("/input") { request ->
            try {
                val result = gameService.inputWord(request).firstOrNull()
                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        result,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (e: Exception) {
                fetchError(e)
            }
        }

        post<ResultGameBody>("/result") { request ->
            try {
                gameService.gameResult(request)
                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        true,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (e: Exception) {
                fetchError(e)
            }
        }

        post<QuestionBody>("/question") { request ->
            try {
                val result = gameService.askQuestion(request).firstOrNull()
                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        result,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (e: Exception) {
                fetchError(e)
            }
        }

        post<QuestionBody>("/question-easy") { request ->
            try {
                val result = gameService.askQuestionForAnswer(request).firstOrNull()
                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        result,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (e: Exception) {
                fetchError(e)
            }
        }
    }
}