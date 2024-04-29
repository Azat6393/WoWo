package data.remote

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.network.tls.CIOCipherSuites
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

actual val ktorHttpClient: HttpClient
    get() = HttpClient(CIO) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    useAlternativeNames = true
                    ignoreUnknownKeys = true
                    encodeDefaults = false
                }
            )
        }
        engine {
            // this: CIOEngineConfig
            maxConnectionsCount = 1000
            endpoint {
                // this: EndpointConfig
                maxConnectionsPerRoute = 100
                pipelineMaxSize = 20
                keepAliveTime = 5000
                connectTimeout = 5000
                connectAttempts = 5
            }
            https {
                serverName = "api.ktor.io"
                cipherSuites = CIOCipherSuites.SupportedSuites
            }
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