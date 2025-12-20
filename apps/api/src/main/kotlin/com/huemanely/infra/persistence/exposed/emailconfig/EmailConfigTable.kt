package com.huemanely.infra.persistence.exposed.emailconfig

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object EmailConfigTable : Table() {
    val id = long("id").autoIncrement()
    val tenantId = uuid("tenant_id")
    
    val provider = varchar("provider", 100)
    val host = varchar("host", 255)
    val port = integer("port")
    val username = varchar("username", 255)
    val password = varchar("password", 255)
    val apiKey = varchar("api_key", 500).nullable()
    val fromAddress = varchar("from_address", 255)
    
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").nullable()

    override val primaryKey = PrimaryKey(id)
}