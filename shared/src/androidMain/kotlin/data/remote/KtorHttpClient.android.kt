package data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
actual val ktorHttpClient: HttpClient
    get() = HttpClient(OkHttp) {
        install(HttpTimeout) {
            socketTimeoutMillis = 60_000
            requestTimeoutMillis = 60_000
        }
        engine {
            config {
                followRedirects(true)
            }
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("Logger Ktor => :$message")
                }
            }
        }

        install(ResponseObserver) {
            onResponse { response ->
                println("HTTP status: ${response.status.value}")
            }
        }

        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }