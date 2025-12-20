package com.huemanely.application.tenant

import org.koin.dsl.module
import com.huemanely.infra.persistence.exposed.tenant.TenantRepository

val tenantModule = module {
    // Define tenant-related dependencies here
    single { TenantRepository()} 

    single { TenantService(get()) }

}