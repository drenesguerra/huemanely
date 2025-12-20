package com.huemanely.presentation.emailconfig

import com.huemanely.infra.persistence.exposed.emailconfig.EmailConfigRepository
import com.huemanely.domain.emailconfig.CreateEmailConfigRequest
import com.huemanely.domain.emailconfig.EmailConfigResponse
import com.huemanely.domain.emailconfig.toResponse

class EmailConfigService(
    private val emailConfigRepository: EmailConfigRepository
) {

    suspend fun create(request: CreateEmailConfigRequest) : EmailConfigResponse{
        return emailConfigRepository.create(request).toResponse()
    }

    suspend fun getByTenantId(tenantId: java.util.UUID): EmailConfigResponse? {
        return emailConfigRepository.findByTenantId(tenantId)?.toResponse()
    }

}