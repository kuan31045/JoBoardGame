package com.kappstudio.joboardgame.data

import android.os.Parcelable
import com.kappstudio.joboardgame.data.room.GameEntity
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.HashMap

@Parcelize
data class Game(
    val id: String = "notFound",
    val name: String = "",
    val image: String = "",
    val type: MutableList<String> = mutableListOf(""),
    val time: Int = 0,
    val tools: List<String> = emptyList(),

    val minPlayerQty: Int = 0,

    val maxPlayerQty: Int = 0,

    val desc: String = "",

    val totalRating: Long = 0,

    val ratingQty: Long = 0,

    val createdTime: Long = Calendar.getInstance().timeInMillis,

    ) : Parcelable {

    fun toEntity(): GameEntity = GameEntity(
        id = id, name = name, image = image, viewedTime = Calendar.getInstance().timeInMillis
    )

    fun toGameMap(): HashMap<String, Any> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "image" to image,
            "minPlayerQty" to minPlayerQty,
            "maxPlayerQty" to maxPlayerQty,
            "time" to time
        )
    }
}

