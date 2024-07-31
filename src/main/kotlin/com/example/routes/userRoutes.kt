package com.example.routes

import com.example.daos.UserDao
import com.example.models.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.userRoutes(userDao: UserDao) {
    route("/user") {
        get("/all") {
            call.respond(status = HttpStatusCode.OK, message = userDao.getAll())
        }

        get("/username_exists") {
            val name = call.request.queryParameters["name"]
            if (name != null) {
                val result = userDao.ifUsernameExist(name)
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/email_exists") {
            val email = call.request.queryParameters["email"]
            if (email != null) {
                val result = userDao.ifUserEmailExist(email)
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        post("/create") {
            val user = call.receive<User>()
            if (userDao.ifUserEmailExist(user.email)) {
                call.respond(HttpStatusCode.Conflict, message = "User with this email already exists.")
            } else if (userDao.ifUsernameExist(user.name)) {
                call.respond(HttpStatusCode.Conflict, message = "User with this username already exists.")
            } else {
                userDao.add(
                    user
                )
                call.respond(HttpStatusCode.OK, "User created successfully.")
            }
        }

        get("/login") {
            val uniqueName = call.request.queryParameters["unique_name"]
            val password = call.request.queryParameters["password"]
            if (uniqueName != null && password != null) {
                val user = userDao.authUser(uniqueName, password)
                if (user != null) {
                    call.respond(HttpStatusCode.OK, user)
                } else {
                    call.respond(HttpStatusCode.Forbidden, "Invalid username or password")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Missing username or password")
            }
        }
    }
}