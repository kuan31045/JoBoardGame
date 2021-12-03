package com.kappstudio.joboardgame.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var image: String = "",
    var status: String = "",
    var county: String = "",
    var friendList: MutableList<String> = mutableListOf(),
    var requestList: MutableList<String> = mutableListOf(),
    var favoriteGames: MutableList<Game> = mutableListOf(),
    var photos: MutableList<String> = mutableListOf(),
): Parcelable
