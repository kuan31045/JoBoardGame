package com.kappstudio.joboardgame.data

import android.os.Parcelable
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.parcelize.Parcelize

@Parcelize
data class Party(
    val id: String = "",
    val hostId: String = UserManager.user.value?.id ?: "",
    val host: User = User(),
    val title: String = "",
    val cover: String = "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/cover1.png?alt=media&token=f3144faf-1e81-4d84-b25e-46e32b64b8f1",
    val partyTime: Long = 0,
    val location: Location = Location(),
    val note: String = "",
    val requirePlayerQty: Int = 0,
    val gameNameList: List<String> = emptyList(),
    val playerIdList: List<String> = listOf(UserManager.user.value?.id ?: ""),
    val playerList: List<User> = emptyList(),
    val photos: List<String> = emptyList(),
) : Parcelable