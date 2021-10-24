package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameMsg(
    val id:String = "",
    var gameName: String = "",
    val gameId: String = "",
    val userId: String = "",
    val user: User = User(),
    val msg:String="",
    val createdTime:Long = 0
) : Parcelable