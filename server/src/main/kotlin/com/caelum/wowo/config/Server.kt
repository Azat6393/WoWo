package com.caelum.wowo.config

import com.caelum.wowo.di.appModule
import com.caelum.wowo.models.response.ErrorResponse
import com.caelum.wowo.utils.InvalidDataException
import com.caelum.wowo.utils.NotFoundException
import com.caelum.wowo.utils.UnknownException
import com.mongodb.MongoCommandException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import org.bson.json.JsonParseException
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level

fun Application.addDefaultApplicationConfiguration() {
    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    install(ContentNegotiation)

    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            call.request.path().startsWith("/")
        }
    }

    install(StatusPages) {

        exception<NotFoundException> { call: ApplicationCall, cause: Throwable ->
            cause.printStackTrace()
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    cause.message!!,
                    HttpStatusCode.NotFound.value
                )
            )
        }

        exception<UnknownException> { call: ApplicationCall, cause: Throwable ->
            cause.printStackTrace()
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    cause.message!!,
                    HttpStatusCode.BadRequest.value
                )
            )
        }

        exception<InvalidDataException> { call: ApplicationCall, cause: Throwable ->
            cause.printStackTrace()
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    cause.message!!,
                    HttpStatusCode.BadRequest.value
                )
            )
        }

        exception<KotlinNullPointerException> { call: ApplicationCall, cause: Throwable ->
            call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse(
                    "Data Not Found",
                    HttpStatusCode.NotFound.value
                )
            )
        }

        exception<JsonParseException> { call: ApplicationCall, cause: Throwable ->
            cause.printStackTrace()
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    "Data Not Found ... Maybe The Json Data Is Invalid",
                    HttpStatusCode.InternalServerError.value
                )
            )
        }

        exception<MongoCommandException> { call: ApplicationCall, cause: Throwable ->
            cause.printStackTrace()
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    "Data Not Found ... DataBase Error ... Maybe The Bson Command Is Invalid",
                    HttpStatusCode.InternalServerError.value
                )
            )
        }
    }
}