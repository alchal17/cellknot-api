package com.example.database

import com.example.models.Users
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun createTables() {
    transaction { SchemaUtils.create(Users) }
}