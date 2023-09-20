package com.kappstudio.joboardgame.data

import android.os.Parcelable
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import java.util.*
import kotlin.collections.HashMap

@Parcelize
data class Rating(
    val id: String = "",
    val gameId: String = "",
    val game: Game = Game(),
    val userId: String = UserManager.user.value?.id?:"",
    val score: Int = 0,
    val createdTime: Long = 0
) : Parcelable

@Parcelize
data class NewRating(
    var id: String = "",
    val gameId: String = "",
    val game: @RawValue HashMap<String, Any> = hashMapOf(
        "id" to "",
        "name" to "",
        "image" to "",
        "minPlayerQty" to 0,
        "maxPlayerQty" to 0,
        "time" to 0L
    ),
    val userId: String = UserManager.user.value?.id?:"",
    val score: Int = 0,
    val createdTime: Long = Calendar.getInstance().timeInMillis
) : Parcelable