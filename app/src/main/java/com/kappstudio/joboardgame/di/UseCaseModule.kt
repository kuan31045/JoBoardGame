package com.kappstudio.joboardgame.di

import com.kappstudio.joboardgame.domain.GetPartiesWithHostUseCase
import com.kappstudio.joboardgame.domain.GetPartyMsgsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {

    singleOf(::GetPartiesWithHostUseCase)

    singleOf(::GetPartyMsgsUseCase)
}