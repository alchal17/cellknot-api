package com.example.koinModules

import com.example.daos.UserDao
import org.koin.dsl.module

val appModule = module {
    single { UserDao() }
}