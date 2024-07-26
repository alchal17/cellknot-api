package com.example.plugins

import com.example.daos.UserDao
import com.example.routes.userRoutes
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
    val userDao: UserDao by inject()
    routing {
        userRoutes(userDao = userDao)
    }
}
