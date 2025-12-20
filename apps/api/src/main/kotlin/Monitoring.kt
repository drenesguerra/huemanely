package com

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.event.*

fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.INFO
        filter { call -> call.request.path().startsWith("/") }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.log.error("Unhandled exception while processing ${call.request.uri}", cause)

            val dev = System.getProperty("io.ktor.development", "false").toBoolean()
            val body = if (dev) {
                // show message + stacktrace in dev only
                "${cause::class.simpleName}: ${cause.message}\n\n${cause.stackTraceToString()}"
            } else {
                "Internal server error"
            }

            call.respondText(body, status = HttpStatusCode.InternalServerError)
        }
    }
    
}
