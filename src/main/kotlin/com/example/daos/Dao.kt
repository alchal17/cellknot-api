package com.example.daos

import com.example.models.Model
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

abstract class Dao<T : Model>(protected val table: IntIdTable) {
    protected abstract fun toEntity(row: ResultRow): T
    abstract suspend fun update(id: Int, entity: T)
    abstract suspend fun add(entity: T)
    suspend fun delete(id: Int) {
        transaction { table.deleteWhere { table.id eq id } }
    }

    suspend fun delete(entity: T) {
        transaction { table.deleteWhere { table.id eq entity.id } }
    }


    suspend fun get(id: Int) = transaction { table.select { table.id eq id }.singleOrNull() }?.let { toEntity(it) }

    suspend fun getAll() = transaction { table.selectAll().toList().map(::toEntity) }

}