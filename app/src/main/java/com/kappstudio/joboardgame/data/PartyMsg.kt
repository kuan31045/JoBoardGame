package com.kappstudio.joboardgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.HashMap

@Parcelize
data class PartyMsg(
    var id: String = "",
    var partyId: String = "",
    var userId: String = "",
    var user: User = User(),
    var msg: String = "",
    val createdTime: Long = 0
) : Parcelable

@Parcelize
data class NewPartyMsg(
    var id: String = "",
    var partyId: String = "",
    var userId: String = UserManager.user["id"] ?: "",
    var user: HashMap<String, String> = UserManager.user,
    var msg: String = "",
    val createdTime: Long = Calendar.getInstance().timeInMillis
) : Parcelable