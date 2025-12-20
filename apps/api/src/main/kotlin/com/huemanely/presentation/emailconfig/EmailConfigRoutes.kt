package com.huemanely.presentation.emailconfig

import com.huemanely.domain.emailconfig.CreateEmailConfigRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*

fun Route.emailConfigRoutes() {

    val emailConfigService by inject<EmailConfigService>()

    route("api/email-configs") {

        post {
            // get tenant id from the plugin attribute
            val tenantUuid = call.attributes[com.TenantKey].tenantId

            val request = call.receive<CreateEmailConfigRequest>().copy(tenantId = tenantUuid)
            val response = emailConfigService.create(request)
            call.respond(HttpStatusCode.Created, response)
        }

        get("{tenantId}") {
            val tenantIdParam = call.parameters["tenantId"]
            if (tenantIdParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing tenantId parameter")
                return@get
            }

            val tenantId = try {
                UUID.fromString(tenantIdParam)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid tenantId format")
                return@get
            }

            val emailConfig = emailConfigService.getByTenantId(tenantId)
            if (emailConfig == null) {
                call.respond(HttpStatusCode.NotFound, "Email configuration not found for tenantId: $tenantId")
            } else {
                call.respond(HttpStatusCode.OK, emailConfig)
            }
        }
    }

}