package com.example.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object Users : IntIdTable() {
    val name = varchar("name", 30)
    val email = varchar("email", 30)
    val password = varchar("password", 15)
    val description = varchar("description", 255).nullable()
    val imagePath = varchar("image_path", 255).nullable()
}

@Serializable
data class User(
    override val id: Int? = null,
    val name: String,
    val email: String,
    val password: String,
    val description: String?,
    @SerialName("avatar_path") val avatarPath: String?
) : Model()