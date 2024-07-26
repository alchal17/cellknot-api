package com.example.daos

import com.example.models.User
import com.example.models.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class UserDao : Dao<User>(table = Users) {
    override fun toEntity(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            name = row[Users.name],
            email = row[Users.email],
            password = row[Users.password],
            description = row[Users.description],
            avatarPath = row[Users.imagePath]
        )
    }

    override suspend fun add(entity: User) {
        transaction {
            Users.insert { row ->
                row[name] = entity.name
                row[email] = entity.email
                row[password] = entity.password
                row[description] = entity.description
                row[imagePath] = entity.avatarPath
            }
        }
    }

    override suspend fun update(id: Int, entity: User) {
        Users.update({ Users.id eq id }) { row ->
            row[name] = entity.name
            row[email] = entity.email
            row[password] = entity.password
            row[description] = entity.description
            row[imagePath] = entity.avatarPath
        }
    }

    suspend fun authUser(uniqueName: String, password: String): User? {
        return transaction {
            Users.select { (Users.name eq uniqueName) or (Users.email eq uniqueName) and (Users.password eq password) }
                .map { resultRow -> toEntity(resultRow) }.firstOrNull()
        }
    }

    suspend fun ifUsernameExist(username: String): Boolean {
        return transaction {
            Users.select { Users.name eq username }.empty().not()
        }
    }

    suspend fun ifUserEmailExist(username: String): Boolean {
        return transaction {
            Users.select { Users.email eq username }.empty().not()
        }
    }
}