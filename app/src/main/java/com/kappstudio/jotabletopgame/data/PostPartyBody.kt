package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostPartyBody(

    var id: String = "",
    var hostId: String = UserManager.user["id"] ?: "",
    var host: HashMap<String, String> = UserManager.user,
    var title: String = "",
    var partyTime: Long = 0,
    var location: String = "",
    var note: String = "",
    var requirePlayerQty: Int = 0,
    var gameNameList: MutableList<String> = mutableListOf(),
    var playerIdList: MutableList<String> = mutableListOf(UserManager.user["id"] ?: ""),
    var playerList: MutableList<HashMap<String, String>> = mutableListOf(
        UserManager.user
    ),


    ) : Parcelable
