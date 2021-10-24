package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartyMsg(
    val id:String = "",
    val partyId: String = "",
    val user: User = User(),
    val msg:String="",
    val createdTime:Long = 0
) : Parcelable