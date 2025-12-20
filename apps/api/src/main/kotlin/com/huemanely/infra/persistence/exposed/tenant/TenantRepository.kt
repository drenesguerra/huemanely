package com.huemanely.infra.persistence.exposed.tenant

import com.huemanely.domain.tenant.CreateTenantRequest
import com.huemanely.domain.tenant.Plan
import com.huemanely.domain.tenant.Tenant
import com.huemanely.domain.tenant.TenantId
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TenantRepository {

    fun create(request: CreateTenantRequest): Tenant = transaction {
        
        val uuid = UUID.randomUUID()
        val slug = generateSlug(request.name)

        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        val insert = TenantTable.insert { row ->
            row[TenantTable.uuid] = uuid
            row[TenantTable.name] = request.name
            row[TenantTable.slug] = slug
            row[TenantTable.domain] = request.domain
            row[TenantTable.plan] = Plan.FREE.name
            row[TenantTable.createdAt] = now
        }

        val generatedId = insert[TenantTable.id]

        Tenant(
            id = TenantId(generatedId),
            uuid = uuid,
            name = request.name,
            slug = slug,
            domain = request.domain,
            plan = Plan.FREE,
            createdAt = now
        )
    }

    fun findByUuid(tenantUuid: UUID): Tenant? = transaction {
        TenantTable
            .selectAll()
            .where { TenantTable.uuid eq tenantUuid }
            .limit(1)
            .map { rowToTenant(it) }
            .singleOrNull()
    }

    fun findAll(): List<Tenant> = transaction {
        TenantTable
            .selectAll()
            .map { rowToTenant(it) }
    }

    fun update(tenant: Tenant): Unit = transaction {
        TenantTable.update({ TenantTable.uuid eq tenant.uuid }) { row ->
            row[TenantTable.name] = tenant.name
            row[TenantTable.slug] = tenant.slug
            row[TenantTable.domain] = tenant.domain
            row[TenantTable.plan] = tenant.plan.name
        }
    }

    fun delete(tenant: Tenant): Unit = transaction {
        TenantTable.deleteWhere { TenantTable.uuid eq tenant.uuid }
    }

    private fun rowToTenant(row: ResultRow): Tenant =
        Tenant(
            id = TenantId(row[TenantTable.id]),
            uuid = row[TenantTable.uuid],
            name = row[TenantTable.name],
            slug = row[TenantTable.slug],
            domain = row[TenantTable.domain],
            plan = try { Plan.valueOf(row[TenantTable.plan]) } catch (_: Exception) { Plan.FREE },
            createdAt = row[TenantTable.createdAt]
        )

    private fun generateSlug(name: String): String =
        name.trim()
            .lowercase()
            .replace(Regex("\\s+"), "-")
            .replace(Regex("[^a-z0-9\\-]"), "")
}