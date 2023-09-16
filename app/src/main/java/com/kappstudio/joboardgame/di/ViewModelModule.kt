package com.kappstudio.joboardgame.di

import com.kappstudio.joboardgame.ui.game.GameViewModel
import com.kappstudio.joboardgame.ui.gamedetail.GameDetailViewModel
import com.kappstudio.joboardgame.ui.login.LoginViewModel
import com.kappstudio.joboardgame.ui.map.MapViewModel
import com.kappstudio.joboardgame.ui.party.PartyViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

val viewModelModule = module {

    viewModelOf(::LoginViewModel)

    viewModelOf(::PartyViewModel)

    viewModelOf(::GameViewModel)

    viewModelOf(::GameDetailViewModel)

    viewModelOf(::MapViewModel)
}