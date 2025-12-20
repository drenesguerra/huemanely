package com.huemanely.application.email

import org.koin.dsl.module
import com.huemanely.infra.persistence.exposed.email.EmailRepository

val emailModule = module {

    // Singleton EmailRepository
    single { EmailRepository() }

    // EmailService that depends on both
    single { EmailService(get(), get()) }
}