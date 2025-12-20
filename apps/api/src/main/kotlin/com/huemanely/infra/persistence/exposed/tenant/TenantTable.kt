package com.huemanely.infra.persistence.exposed.tenant

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.CurrentDateTime
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object TenantTable : Table("tenants") {
    val id = long("id").autoIncrement()
    val uuid = uuid("uuid").uniqueIndex()
    
    val name = varchar("name", 255)
    val slug = varchar("slug", 100).uniqueIndex()
    val domain = varchar("domain", 255).nullable()
    val plan = varchar("plan", 50)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)

    override val primaryKey = PrimaryKey(id)
}