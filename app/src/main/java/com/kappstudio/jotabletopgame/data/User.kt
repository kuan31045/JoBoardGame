package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    var id: String = "",
    var name: String = "",
    var picture: String = "",
    var introduction: String = "",
    var favoriteGames: MutableList<String>? = null,
    var recentlyViewed: MutableList<String>? = null
): Parcelable