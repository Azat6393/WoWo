package com.caelum_software.wowo.controller

import com.caelum_software.wowo.models.body.InsertCategoryBody
import com.caelum_software.wowo.models.body.InsertWordBody
import com.caelum_software.wowo.models.response.SuccessResponse
import com.caelum_software.wowo.service.AdminService
import com.caelum_software.wowo.utils.ApiPaths
import com.caelum_software.wowo.utils.fetchError
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.coroutines.flow.firstOrNull
import org.koin.ktor.ext.inject

fun Route.adminRouter() {
    val adminService by inject<AdminService>()

    route(ApiPaths.ADMIN) {

        post<InsertCategoryBody>("/category") { request ->
            try {
                val result = adminService.insertCategory(
                    category = request.category,
                    language = request.language
                ).firstOrNull()
                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        result,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (exception: Exception) {
                fetchError(exception)
            }
        }

        post<InsertWordBody>("/word") { request ->
            try {
                val result = adminService.insertWord(
                    words = request.words,
                    category = request.category,
                    language = request.language,
                    difficulty = request.difficulty
                )
                call.respond(
                    HttpStatusCode.OK,
                    SuccessResponse(
                        result,
                        HttpStatusCode.OK.value,
                        "Success"
                    )
                )
            } catch (exception: Exception) {
                fetchError(exception)
            }
        }
    }
}