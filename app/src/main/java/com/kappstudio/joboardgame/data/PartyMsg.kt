package com.kappstudio.joboardgame.data

import android.os.Parcelable
import com.kappstudio.joboardgame.login.UserManager
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
    var userId: String = UserManager.user.value?.id?:"",
    var user: HashMap<String, String> = toUserMap(UserManager.user.value?:User()),
    var msg: String = "",
    val createdTime: Long = Calendar.getInstance().timeInMillis
) : Parcelable