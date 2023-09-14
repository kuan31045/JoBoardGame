package com.kappstudio.joboardgame.di


import com.kappstudio.joboardgame.ui.login.LoginViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {
    viewModel {
        LoginViewModel(get())
    }
}