package com.caelum.wowo.controller

import com.caelum.wowo.models.body.InsertCategoryBody
import com.caelum.wowo.models.body.InsertWordBody
import com.caelum.wowo.service.AdminService
import com.caelum.wowo.utils.ApiPaths
import data.remote.response.SuccessResponse
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
        }

        post<InsertWordBody>("/word") { request ->
            val result = adminService.insertWord(
                words = request.words,
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
        }
    }
}