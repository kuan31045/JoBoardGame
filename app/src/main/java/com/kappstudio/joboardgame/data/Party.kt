package com.kappstudio.joboardgame.data

import android.os.Parcelable
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.parcelize.Parcelize

@Parcelize
data class Party(
    var id: String = "",
    var hostId: String = UserManager.user.value?.id?:"",
    var title: String = "",
    var cover: String = "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/cover1.png?alt=media&token=f3144faf-1e81-4d84-b25e-46e32b64b8f1",
    var partyTime: Long = 0,
    var location:  Location =  Location(),
    var note: String = "",
    var requirePlayerQty: Int = 0,
    var gameNameList: MutableList<String> = mutableListOf(),
    var playerIdList: MutableList<String> = mutableListOf(UserManager.user.value?.id?:""),
    var photos: MutableList<String> = mutableListOf(),
    ) : Parcelable
