package com

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.statement.bodyAsText
import kotlin.test.Test
import kotlin.test.assertEquals
import com.huemanely.infra.persistence.exposed.tenant.TenantRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID
import org.slf4j.event.*
import com.huemanely.config.UUIDSerializer
import io.ktor.client.call.*
// Removed invalid import for contentnegotiation plugin
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*
import com.huemanely.config.DatabaseFactory

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {

        application {
            DatabaseFactory.init()
            configureSerialization()
            configureHTTP()
            configureMonitoring()
            configureRouting()
        }

        client.get("/api/tenants").apply {
            println(bodyAsText())
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }

        client.get("/api/tenants/123").apply {
            println(bodyAsText())
            assertEquals(HttpStatusCode.InternalServerError, status)
        }
        
    }

}
