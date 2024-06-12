package com.caelum_software.wowo.config

import com.caelum_software.wowo.di.appModule
import com.caelum_software.wowo.utils.exception.InvalidDataException
import com.caelum_software.wowo.utils.exception.NotFoundException
import com.caelum_software.wowo.utils.exception.UnknownException
import com.caelum_software.wowo.models.response.ErrorResponse
import com.mongodb.MongoCommandException
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.request.path
import io.ktor.server.response.respond
import org.bson.json.JsonParseException
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.event.Level
import kotlin.time.Duration.Companion.seconds

fun Application.addDefaultApplicationConfiguration() {

    install(Koin) {
        slf4jLogger()
        modules(appModule)
    }

    install(ContentNegotiation){
        json()
    }

    install(RateLimit){
        global {
            rateLimiter(limit = 40, refillPeriod = 60.seconds)
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            call.request.path().startsWith("/")
        }
    }

    install(CORS) {
        methods.add(HttpMethod.Options)
        methods.add(HttpMethod.Put)
        methods.add(HttpMethod.Post)
        methods.add(HttpMethod.Get)
        methods.add(HttpMethod.Delete)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
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
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    cause.message!!,
                    HttpStatusCode.InternalServerError.value
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

        exception<KotlinNullPointerException> { call: ApplicationCall, _: Throwable ->
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