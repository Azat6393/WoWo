package com.caelum_software.wowo

import com.caelum_software.wowo.config.addDefaultApplicationConfiguration
import com.caelum_software.wowo.controller.adminRouter
import com.caelum_software.wowo.controller.gameRouter
import com.caelum_software.wowo.controller.userRouter
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(
        Netty,
        port = (System.getenv("PORT") ?: "8080").toInt(),
        module = Application::module
    )
        .start(wait = true)
}

fun Application.module() {
    addDefaultApplicationConfiguration()

    routing {
        gameRouter()
        adminRouter()
        userRouter()
    }
}