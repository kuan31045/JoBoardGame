package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Rating(
    val id: String = "",
    val gameId: String = "",
    val game: Game = Game(),
    val userId: String = "",
    val score: Int = 1,
    val createdTime: Long = 0
) : Parcelable