package com.kappstudio.joboardgame.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val status: String = "",
    val county: String = "",
    val friendList: MutableList<String> = mutableListOf(),
    val requestList: MutableList<String> = mutableListOf(),
    val favoriteGames: MutableList<Game> = mutableListOf(),
    val photos: MutableList<String> = mutableListOf(),
): Parcelable