package com.huemanely.infra.persistence.exposed.email

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object EmailTable : Table("emails") {
    val id = long("id").autoIncrement()
    val uuid = uuid("uuid").uniqueIndex()
    val tenantId = uuid("tenant_id").index()

    val from = varchar("from", 255)
    val to = varchar("to", 255)
    val subject = varchar("subject", 255)
    val body = text("body")
    val status = varchar("status", 50)

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val sentAt = datetime("sent_at").nullable()
    val error = text("error").nullable()

    override val primaryKey = PrimaryKey(id)
}