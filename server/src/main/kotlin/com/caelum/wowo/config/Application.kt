package com.caelum.wowo.config

import com.caelum.wowo.controller.adminRouter
import com.caelum.wowo.controller.gameRouter
import com.caelum.wowo.controller.userRouter
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(
        Netty,
        port = (System.getenv("PORT") ?: "5000").toInt(),
        host = "0.0.0.0",
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    routing {
        gameRouter()
        adminRouter()
        userRouter()
    }

    addDefaultApplicationConfiguration()
}