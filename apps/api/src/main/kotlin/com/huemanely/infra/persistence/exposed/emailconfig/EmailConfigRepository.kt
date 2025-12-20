package com.huemanely.infra.persistence.exposed.emailconfig

import com.huemanely.domain.emailconfig.EmailConfig
import com.huemanely.domain.emailconfig.EmailProvider
import com.huemanely.domain.emailconfig.EmailConfigId
import com.huemanely.domain.emailconfig.CreateEmailConfigRequest
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class EmailConfigRepository {
    
    fun create(request: CreateEmailConfigRequest): EmailConfig = transaction {

        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        val insert = EmailConfigTable.insert { row ->
            row[tenantId] = request.tenantId
            row[provider] = request.provider.name
            row[host] = request.host
            row[port] = request.port
            row[username] = request.username
            row[password] = request.password
            row[apiKey] = request.apiKey
            row[fromAddress] = request.fromAddress
            row[createdAt] = now
        }

        val generatedId = insert[EmailConfigTable.id]

        EmailConfig(
            id = EmailConfigId(generatedId),
            tenantId = request.tenantId,
            provider = request.provider.name.let { EmailProvider.valueOf(it) },
            host = request.host,
            port = request.port,
            username = request.username,
            password = request.password,
            apiKey = request.apiKey,
            fromAddress = request.fromAddress,
            createdAt = now
        )
    }

    fun findByTenantId(tenantId: UUID): EmailConfig? = transaction {
        EmailConfigTable.selectAll()
            .where { EmailConfigTable.tenantId eq tenantId }
            .limit(1)
            .map { rowEmailConfig(it) }
            .singleOrNull()
    }

    private fun rowEmailConfig(row: ResultRow): EmailConfig {
        return EmailConfig(
            id = EmailConfigId(row[EmailConfigTable.id]),
            tenantId = row[EmailConfigTable.tenantId],
            provider = EmailProvider.valueOf(row[EmailConfigTable.provider]),
            host = row[EmailConfigTable.host],
            port = row[EmailConfigTable.port],
            username = row[EmailConfigTable.username],
            password = row[EmailConfigTable.password],
            apiKey = row[EmailConfigTable.apiKey],
            fromAddress = row[EmailConfigTable.fromAddress],
            createdAt = row[EmailConfigTable.createdAt],
            updatedAt = row[EmailConfigTable.updatedAt]
        )
    }

}