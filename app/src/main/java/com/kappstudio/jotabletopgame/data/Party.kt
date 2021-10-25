package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Party(
    val id: String = "",
    var hostId: String = "",
    var host: User = User(),
    var title: String = "",
    var partyTime: Long = 0,
    var location: String = "",
    var note: String = "",
    var requirePlayerQty: Int = 0,
    var gameNameList: MutableList<String> = mutableListOf(),
    var playerIdList: MutableList<String> = mutableListOf(),
    var playerList: MutableList<User>? = null,

    ) : Parcelable

