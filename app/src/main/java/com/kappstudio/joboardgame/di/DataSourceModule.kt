package com.kappstudio.joboardgame.di

import androidx.room.Room
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.data.repository.GameRepositoryImpl
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.data.repository.PartyRepositoryImpl
import com.kappstudio.joboardgame.data.repository.StorageRepository
import com.kappstudio.joboardgame.data.repository.StorageRepositoryImpl
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.data.repository.UserRepositoryImpl
import com.kappstudio.joboardgame.data.room.JoDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dbModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            JoDatabase::class.java,
            JoDatabase::class.java.simpleName
        ).fallbackToDestructiveMigration()
            .build()
    }
}

val daoModule = module {
    single { get<JoDatabase>().gameDao() }
}

val repositoryModule = module {

    single<UserRepository> { UserRepositoryImpl() }

    single<PartyRepository> { PartyRepositoryImpl() }

    single<GameRepository> { GameRepositoryImpl(get()) }

    single<StorageRepository> { StorageRepositoryImpl() }
}