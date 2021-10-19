package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val type: MutableList<String>? = null,
    val time: Long = 0,
    val minPlayerLimit: Int = 0,
    val maxPlayerLimit: Int = 0,
    val desc: String = "",
    var totalRating: Long? = null,
    var ratingQty: Long? = null,
): Parcelable