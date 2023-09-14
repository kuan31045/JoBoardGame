package com.kappstudio.joboardgame.di

import com.kappstudio.joboardgame.data.source.repository.PartyRepository
import com.kappstudio.joboardgame.data.source.repository.PartyRepositoryImpl
import com.kappstudio.joboardgame.data.source.repository.UserRepository
import com.kappstudio.joboardgame.data.source.repository.UserRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {

    single<UserRepository> {
        UserRepositoryImpl()
    }

    single<PartyRepository> {
        PartyRepositoryImpl()
    }
}