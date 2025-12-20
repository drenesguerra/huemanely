package com

import io.ktor.server.application.*
import com.huemanely.config.DatabaseFactory
import com.huemanely.application.email.emailModule
import com.huemanely.application.tenant.tenantModule
import com.huemanely.application.emailconfig.emailConfigModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    
    DatabaseFactory.init()
    
    configureTenantPlugin()

    install(Koin) {
        slf4jLogger()
        modules(emailModule)
        modules(tenantModule)
        modules(emailConfigModule)
    }

    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureRouting()
}
