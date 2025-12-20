package com.huemanely.domain.tenant

import java.util.UUID
import java.time.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Serializable
@JvmInline
value class TenantId(val value: Long)

@Serializable
data class Tenant(

    val id: TenantId,
    @Contextual
    val uuid: UUID = UUID.randomUUID(),

    val name: String,
    val slug: String,
    val domain: String? = null,
    val plan: Plan = Plan.FREE,
    
    val createdAt: LocalDateTime
)

enum class Plan { FREE, STARTER, PRO, ENTERPRISE }

@Serializable
data class CreateTenantRequest(
    val name: String,
    val domain: String
)

@Serializable
data class UpdateTenantRequest(
    val name: String? = null,
    val domain: String? = null,
    val isActive: Boolean? = null
)

@Serializable
data class TenantResponse(
    val uuid: String,
    val name: String,
    val slug: String,
    val domain: String? = null,
    val plan: String
)

fun Tenant.toResponse(): TenantResponse = TenantResponse(
    uuid = this.uuid.toString(),
    name = this.name,
    slug = this.slug,
    domain = this.domain,
    plan = this.plan.name
)

fun List<Tenant>.toResponseList(): List<TenantResponse> = this.map { it.toResponse() }