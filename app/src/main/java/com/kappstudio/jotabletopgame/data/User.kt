package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var image: String = "",
    var status: String = "",
    var favoriteGames: MutableList<Game>? = null,
    var recentlyViewed: MutableList<Game>? = null
): Parcelable