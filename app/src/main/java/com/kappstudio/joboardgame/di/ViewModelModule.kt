package com.kappstudio.joboardgame.di

import com.kappstudio.joboardgame.ui.favorite.FavoriteViewModel
import com.kappstudio.joboardgame.ui.friend.FriendViewModel
import com.kappstudio.joboardgame.ui.game.GameViewModel
import com.kappstudio.joboardgame.ui.gamedetail.GameDetailViewModel
import com.kappstudio.joboardgame.ui.login.LoginViewModel
import com.kappstudio.joboardgame.ui.map.MapViewModel
import com.kappstudio.joboardgame.ui.myparty.MyPartyViewModel
import com.kappstudio.joboardgame.ui.myrating.MyRatingViewModel
import com.kappstudio.joboardgame.ui.party.PartyViewModel
import com.kappstudio.joboardgame.ui.partydetail.PartyDetailViewModel
import com.kappstudio.joboardgame.ui.rating.RatingViewModel
import com.kappstudio.joboardgame.ui.user.UserViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModelOf

val viewModelModule = module {

    viewModelOf(::LoginViewModel)

    viewModelOf(::PartyViewModel)

    viewModelOf(::GameViewModel)

    viewModelOf(::GameDetailViewModel)

    viewModelOf(::MapViewModel)

    viewModelOf(::RatingViewModel)

    viewModelOf(::PartyDetailViewModel)

    viewModelOf(::UserViewModel)

    viewModelOf(::MyPartyViewModel)

    viewModelOf(::FriendViewModel)

    viewModelOf(::MyRatingViewModel)

    viewModelOf(::FavoriteViewModel)
}