package com

import com.huemanely.presentation.tenant.tenantRoutes
import com.huemanely.presentation.email.emailRoutes
import com.huemanely.presentation.emailconfig.emailConfigRoutes
import com.huemanely.application.tenant.TenantService
import com.huemanely.application.email.EmailService
import com.huemanely.application.email.SmtpEmailSender
import com.huemanely.infra.persistence.exposed.tenant.TenantRepository
import com.huemanely.infra.persistence.exposed.email.EmailRepository
import com.huemanely.config.UUIDSerializer

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

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import io.ktor.serialization.kotlinx.json.*
import java.util.UUID

fun Application.configureRouting() {
    
    routing {
        get("/") {
            call.respondText("Hello World!")
        }

        // old way without DI
        // val tenantRepo = TenantRepository()
        // val tenantService = TenantService(tenantRepo)
        // tenantRoutes(tenantService)

        // new way with DI
        tenantRoutes()
        emailRoutes()
        emailConfigRoutes()

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static")
    }
}