package com.huemanely.domain.email

import java.util.UUID
import java.time.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Serializable
@JvmInline
value class EmailId(val value: Long)

@Serializable
data class Email(

    val id: EmailId,
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    @Contextual
    val tenantId: UUID? = null,

    val from: String? = null,
    val to: String? = null,
    val subject: String? = null,
    val body: String? = null,
    val status: EmailStatus = EmailStatus.PENDING,

    val createdAt: LocalDateTime,
    val sentAt: LocalDateTime? = null,
    val error: String? = null

)

enum class EmailStatus {
    PENDING,
    SENT,
    FAILED
}

@Serializable
data class CreateEmailRequest(
    @Contextual
    val tenantId: UUID? = null,
    val from: String,
    val to: String,
    val subject: String,
    val body: String

)

@Serializable
data class UpdateEmailRequest(

    val from: String?,
    val to: String?,
    val subject: String?,
    val body: String?,
    val status: EmailStatus?

)

@Serializable
data class EmailResponse(
    @Contextual
    val uuid: String,
    val from: String,
    val to: String,
    val subject: String,
    val body: String,
    val status: String

)

fun Email.toResponse(): EmailResponse = EmailResponse(
    uuid = this.uuid.toString(),
    from = this.from ?: "",
    to = this.to ?: "",
    subject = this.subject ?: "",
    body = this.body ?: "",
    status = this.status.name
)

fun List<Email>.toResponseList(): List<EmailResponse> = this.map { it.toResponse() }