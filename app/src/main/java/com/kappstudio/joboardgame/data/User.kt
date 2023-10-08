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
    val friendList: List<String> = emptyList(),
    val requestList: List<String> = emptyList(),
    val favoriteGames: List<Game> = emptyList(),
    val photos: List<String> = emptyList(),
): Parcelable