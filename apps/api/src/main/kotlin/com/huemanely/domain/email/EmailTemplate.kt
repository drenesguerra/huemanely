package com.huemanely.domain.email

import java.util.UUID
import java.time.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Serializable
@JvmInline
value class EmailTemplateId(val value: Long)

data class EmailTemplate(
    val id: EmailTemplateId,
    @Contextual
    val uuid: UUID = UUID.randomUUID(),
    val tenantId: UUID,
    val name: String,
    val subject: String,
    val body: String
)