package com

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.util.*
import java.util.UUID

val TenantKey = AttributeKey<TenantContext>("TenantContext")

fun ApplicationCall.tenantId(): UUID =
    attributes[TenantKey].tenantId

fun Application.configureTenantPlugin() {
    install(TenantPlugin)
}
val TenantPlugin = createApplicationPlugin(name = "TenantPlugin") {
    onCall { call ->
        val path = call.request.path()
        // Skip tenant check for the tenant-management endpoint(s)
        if (path.startsWith("/api/tenants")) {
            return@onCall
        }

        // enforce tenant header for all other routes
        val header = call.request.headers["X-Tenant-ID"]
        val tenantId = header?.let {
            try {
                UUID.fromString(it)
            } catch (e: IllegalArgumentException) {
                null
            }
        } ?: error("Missing or invalid X-Tenant-ID header")

        call.attributes.put(TenantKey, TenantContext(tenantId))
    }
}