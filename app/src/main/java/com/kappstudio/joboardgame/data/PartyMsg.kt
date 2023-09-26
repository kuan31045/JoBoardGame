package com.kappstudio.joboardgame.data

import android.os.Parcelable
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.parcelize.Parcelize
import java.util.Calendar

@Parcelize
data class PartyMsg(
    val id: String = "",
    val partyId: String = "",
    val userId: String = UserManager.user.value?.id ?: "",
    val user: User = User(),
    val msg: String = "",
    val createdTime: Long = Calendar.getInstance().timeInMillis,
) : Parcelable