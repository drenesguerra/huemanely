package com.huemanely.config

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.h2.tools.Server
import com.huemanely.infra.persistence.exposed.tenant.TenantTable
import com.huemanely.infra.persistence.exposed.email.EmailTable

object DatabaseFactory {

    private var tcpServer: Server? = null
    private var webServer: Server? = null

    fun init() {
        // start TCP and web console so external tools can connect to the in-memory DB
        tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start()
        webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start()

        // connect via TCP to the same in-memory database name "huemanely"
        Database.connect(
            url = "jdbc:h2:mem:huemanely;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=10000",
            driver = "org.h2.Driver",
            user = "sa",
            password = ""
        )

        transaction {
            SchemaUtils.create(TenantTable)
            SchemaUtils.create(EmailTable)
        }
    }

    fun stop() {
        tcpServer?.stop()
        webServer?.stop()
    }
    
}