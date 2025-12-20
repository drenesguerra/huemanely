package com.huemanely.infra.persistence.exposed.email

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*
import com.huemanely.domain.email.*
import com.huemanely.infra.persistence.exposed.email.EmailTable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class EmailRepository {

    fun create(request: CreateEmailRequest): Email = transaction {

        val uuid = UUID.randomUUID()

        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        
        val insert = EmailTable.insert { row ->
            row[EmailTable.uuid] = uuid
            row[EmailTable.tenantId] = UUID.fromString(request.tenantId.toString())
            row[EmailTable.from] = request.from
            row[EmailTable.to] = request.to
            row[EmailTable.subject] = request.subject
            row[EmailTable.body] = request.body
            row[EmailTable.status] = EmailStatus.PENDING.name
            row[EmailTable.createdAt] = now
        }

        val generatedId = insert[EmailTable.id]

        Email(
            id = EmailId(generatedId),
            uuid = uuid,
            tenantId = UUID.fromString(request.tenantId.toString()),
            from = request.from,
            to = request.to,
            subject = request.subject,
            body = request.body,
            status = EmailStatus.PENDING,
            createdAt = now
        )

    }

    fun findByUuid(uuid: UUID): Email? = transaction {
        EmailTable.selectAll()
            .where { EmailTable.uuid eq uuid }
            .limit(1)
            .map { rowToEmail(it) }
            .singleOrNull()
    }

    fun update(email: Email): Unit = transaction {
        EmailTable.update({ EmailTable.uuid eq email.uuid }) { row ->
            row[EmailTable.from] = email.from !!
            row[EmailTable.to] = email.to !!
            row[EmailTable.subject] = email.subject !!
            row[EmailTable.body] = email.body !!
            row[EmailTable.status] = email.status.name
        }
    }

    fun updateStatus(id: EmailId, status: EmailStatus, error: String? = null): Unit = transaction {
        EmailTable.update({ EmailTable.id eq id.value }) { row ->
            row[EmailTable.status] = status.name
            if (error != null) {
                row[EmailTable.error] = error
            }
        }
    }

    fun findByTenant(tenantId: UUID): List<Email> = transaction {
        EmailTable.selectAll()
            .where { EmailTable.tenantId eq tenantId }
            .map { rowToEmail(it) }
    }

    private fun rowToEmail(row: ResultRow): Email = Email(
        id = EmailId(row[EmailTable.id]),
        uuid = row[EmailTable.uuid],
        tenantId = row[EmailTable.tenantId],
        from = row[EmailTable.from],
        to = row[EmailTable.to],
        subject =  row[EmailTable.subject],
        body = row[EmailTable.body],
        status = EmailStatus.valueOf(row[EmailTable.status]),
        createdAt = row[EmailTable.createdAt],
        error = row[EmailTable.error]
    )

}