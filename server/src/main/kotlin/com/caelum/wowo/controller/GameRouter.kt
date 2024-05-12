package com.caelum.wowo.controller

import com.caelum.wowo.models.body.InputWordBody
import com.caelum.wowo.models.body.QuestionBody
import com.caelum.wowo.models.body.ResultGameBody
import com.caelum.wowo.models.response.SuccessResponse
import com.caelum.wowo.service.GameService
import com.caelum.wowo.utils.ApiPaths
import com.caelum.wowo.utils.fetchError
import io.ktor.client.request.request
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
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
                val language = call.parameters["language"] ?: return@get call.respondText(
                    text = "Missing language",
                    status = HttpStatusCode.BadRequest
                )
                val category = call.parameters["category"] ?: return@get call.respondText(
                    text = "Missing category",
                    status = HttpStatusCode.BadRequest
                )
                val difficulty = call.parameters["difficulty"] ?: return@get call.respondText(
                    text = "Missing difficulty",
                    status = HttpStatusCode.BadRequest
                )
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
                val language = call.parameters["language"] ?: return@get call.respondText(
                    text = "Missing language",
                    status = HttpStatusCode.BadRequest
                )
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