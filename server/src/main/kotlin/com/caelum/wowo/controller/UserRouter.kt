package com.caelum.wowo.controller

import com.caelum.wowo.service.UserService
import com.caelum.wowo.utils.ApiPaths
import com.caelum.wowo.models.response.SuccessResponse
import com.caelum.wowo.models.wowo.User
import com.caelum.wowo.utils.fetchError
import com.caelum.wowo.utils.responseError
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

fun Route.userRouter() {
    val userService by inject<UserService>()

    route(ApiPaths.USERS) {
        get("/statistics/{user_id?}") {
            try {
                val userId = call.parameters["user_id"] ?: return@get responseError("Missing user id")
                val result = userService.getUserStatistics(userId).firstOrNull()
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

        get("/{user_id?}") {
            try {
                val userId = call.parameters["user_id"] ?: return@get responseError("Missing user id")
                val result = userService.getUser(userId).firstOrNull()
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

        post<User>("/") { request ->
            try {
                val result = userService.updateUser(request).firstOrNull()
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