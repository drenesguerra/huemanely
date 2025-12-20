package com.huemanely.domain.emailconfig

import java.util.UUID
import java.time.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Serializable
@JvmInline
value class EmailConfigId(val value: Long)

@Serializable
data class EmailConfig(

    val id: EmailConfigId,
    @Contextual
    val tenantId: UUID,

    val provider: EmailProvider,
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val apiKey: String? = null,
    val fromAddress: String,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null

)

enum class EmailProvider { SMTP, AWS_SES, SENDGRID }

@Serializable
data class CreateEmailConfigRequest(

    @Contextual
    val tenantId: UUID,

    val provider: EmailProvider,
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val apiKey: String? = null,
    val fromAddress: String

)

@Serializable
data class UpdateEmailConfigRequest(

    @Contextual
    val tenantId: UUID,

    val provider: EmailProvider,
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val apiKey: String? = null,
    val fromAddress: String

)

@Serializable
data class EmailConfigResponse(

    val id: Long,
    @Contextual
    val tenantId: UUID,

    val provider: EmailProvider,
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val apiKey: String? = null,
    val fromAddress: String,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null

)

fun EmailConfig.toResponse(): EmailConfigResponse {
    return EmailConfigResponse(
        id = this.id.value,
        tenantId = this.tenantId,
        provider = this.provider,
        host = this.host,
        port = this.port,
        username = this.username,
        password = this.password,
        apiKey = this.apiKey,
        fromAddress = this.fromAddress,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}

fun List<EmailConfig>.toResponseList(): List<EmailConfigResponse> {
    return this.map { it.toResponse() }
}