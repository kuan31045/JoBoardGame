package com.kappstudio.joboardgame.di

import com.kappstudio.joboardgame.domain.GetPartyWithHostUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetPartyWithHostUseCase)
}