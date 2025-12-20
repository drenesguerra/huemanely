package com.huemanely.application.emailconfig

import org.koin.dsl.module
import com.huemanely.infra.persistence.exposed.emailconfig.EmailConfigRepository
import com.huemanely.presentation.emailconfig.EmailConfigService

val emailConfigModule = module {

    // Singleton EmailConfigRepository
    single { EmailConfigRepository() }

    // EmailConfigService that depends on EmailConfigRepository
    single { EmailConfigService(get()) }
}