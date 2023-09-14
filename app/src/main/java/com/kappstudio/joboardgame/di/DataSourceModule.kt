package com.kappstudio.joboardgame.di

import com.kappstudio.joboardgame.data.source.repository.UserRepository
import com.kappstudio.joboardgame.data.source.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    single<UserRepository> {
        UserRepositoryImpl()
    }
}