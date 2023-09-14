package com.kappstudio.joboardgame.di


import com.kappstudio.joboardgame.ui.login.LoginViewModel
import com.kappstudio.joboardgame.ui.party.PartyViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {

    viewModel { LoginViewModel(get()) }
    viewModel { PartyViewModel(get(), get()) }
}