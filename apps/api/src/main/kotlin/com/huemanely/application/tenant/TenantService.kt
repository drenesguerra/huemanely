package com.huemanely.application.tenant

import com.huemanely.domain.tenant.*
import com.huemanely.infra.persistence.exposed.tenant.TenantRepository
import java.util.UUID

class TenantService(
    private val repository: TenantRepository
) {
    
    suspend fun create(request: CreateTenantRequest): TenantResponse {
        return repository.create(request).toResponse()
    }

    suspend fun findByUuid(uuid: UUID): TenantResponse? {
        return repository.findByUuid(uuid)?.toResponse()
    }

    suspend fun findAll(): List<TenantResponse> {
        val tenants = repository.findAll()
        return tenants.toResponseList()
    }

    suspend fun update(uuid: UUID, request: UpdateTenantRequest): TenantResponse? {
        val existingTenant = repository.findByUuid(uuid) ?: return null
        val updatedTenant = existingTenant.copy(name = request.name ?: existingTenant.name)
        repository.update(updatedTenant)
        return repository.findByUuid(uuid)?.toResponse()
    }

    suspend fun delete(uuid: UUID): Boolean {
        val existingTenant = repository.findByUuid(uuid) ?: return false
        repository.delete(existingTenant)
        return true
    }   

}