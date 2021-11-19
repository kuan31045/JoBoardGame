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
    var userId: String = UserManager.user.value?.id?:"",
    var msg: String = "",
    val createdTime: Long = Calendar.getInstance().timeInMillis
) : Parcelable

