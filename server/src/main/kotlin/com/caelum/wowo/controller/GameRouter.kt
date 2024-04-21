package com.caelum.wowo.controller

import com.caelum.wowo.models.body.InputWordBody
import com.caelum.wowo.models.body.QuestionBody
import com.caelum.wowo.models.response.SuccessResponse
import com.caelum.wowo.service.GameService
import com.caelum.wowo.utils.ApiPaths
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

        get("/word/{category?}/{language?}") {
            val language = call.parameters["language"] ?: return@get call.respondText(
                text = "Missing language",
                status = HttpStatusCode.BadRequest
            )
            val category = call.parameters["category"] ?: return@get call.respondText(
                text = "Missing category",
                status = HttpStatusCode.BadRequest
            )
            val result = gameService.getRandomWord(
                language = language,
                category = category
            ).firstOrNull()
            call.respond(
                HttpStatusCode.OK,
                SuccessResponse(
                    result,
                    HttpStatusCode.OK.value,
                    "Success"
                )
            )
        }

        post<InputWordBody>("/input") { request ->
            val result = gameService.inputWord(request).firstOrNull()
            call.respond(
                HttpStatusCode.OK,
                SuccessResponse(
                    result,
                    HttpStatusCode.OK.value,
                    "Success"
                )
            )
        }

        post<QuestionBody>("/question") { request ->
            val result = gameService.askQuestion(request).firstOrNull()
            call.respond(
                HttpStatusCode.OK,
                SuccessResponse(
                    result,
                    HttpStatusCode.OK.value,
                    "Success"
                )
            )
        }
    }
}