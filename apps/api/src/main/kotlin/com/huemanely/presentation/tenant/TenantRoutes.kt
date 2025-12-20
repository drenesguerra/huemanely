package com.huemanely.presentation.tenant

import com.huemanely.application.tenant.TenantService
import com.huemanely.domain.tenant.*
import java.util.UUID
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import io.ktor.server.plugins.swagger.*
import io.ktor.server.plugins.openapi.*
import io.swagger.codegen.v3.generators.html.*

fun Route.tenantRoutes() {

    val tenantService by inject<TenantService>()

    route("api/tenants") {

        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml") {
            codegen = StaticHtmlCodegen()
        }

        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }

        get () {
            val response = tenantService.findAll()
            call.respond(HttpStatusCode.OK, response)
        }

        post {
            val request = call.receive<CreateTenantRequest>()
            val response = tenantService.create(request)
            call.respond(HttpStatusCode.Created, response)
        }
        
        get("/{uuid}") {
            val uuidParam = call.parameters["uuid"]
            if (uuidParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid UUID")
                return@get
            }
            val response = tenantService.findByUuid(UUID.fromString(uuidParam))
            if (response == null) {
                call.respond(HttpStatusCode.NotFound, "Tenant not found")
            } else {
                call.respond(HttpStatusCode.OK, response)
            }
        }

        //update tenant, delete tenant, list tenants etc. can be added here
        put ("/{uuid}") {
            // Implementation for updating a tenant
            val uuidParam = call.parameters["uuid"]
            if (uuidParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid UUID")
                return@put
            }
            val request = call.receive<UpdateTenantRequest>()
            val response = tenantService.update(UUID.fromString(uuidParam), request)
            if (response == null) {
                call.respond(HttpStatusCode.NotFound, "Tenant not found")
            } else {
                call.respond(HttpStatusCode.OK, response)
            }
        }

        delete("/{uuid}") {
            val uuidParam = call.parameters["uuid"]
            if (uuidParam == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid UUID")
                return@delete
            }
            // Assuming a deleteTenant method exists in TenantService
            tenantService.delete(UUID.fromString(uuidParam))
            call.respond(HttpStatusCode.NoContent)
        }

    }
}