package com.huemanely.presentation.email

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import java.util.*
import com.huemanely.application.email.EmailService
import com.huemanely.domain.email.CreateEmailRequest

fun Route.emailRoutes() {

    val emailService by inject<EmailService>()

    route("api/emails") {

        post {
            // get tenant id from the plugin attribute
            val tenantUuid = call.attributes[com.TenantKey].tenantId

            // receive request and attach tenant id (assumes CreateEmailRequest has tenantId property)
            val request = call.receive<CreateEmailRequest>().copy(tenantId = tenantUuid)

            // pass request (with tenant) to service
            emailService.createEmail(request)

            call.respondText("Email queued for sending.")
        }
        

        get("/by-tenant") {
            val tenantUuid = call.attributes[com.TenantKey].tenantId
            val emails = emailService.getEmailByTenant(tenantUuid)
            // call.respondText("Listing emails for tenant: $tenantUuid")
            call.respond(emails)
        }

    }
}