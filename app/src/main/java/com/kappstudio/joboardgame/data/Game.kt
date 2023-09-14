package com.kappstudio.joboardgame.data

import android.os.Parcelable
import com.kappstudio.joboardgame.data.room.GameEntity
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.HashMap

@Parcelize
data class Game(
    val id: String = "notFound",
    var name: String = "",
    var image: String = "",
    var type: MutableList<String> = mutableListOf(""),
    var time: Int = 0,
    var tools: List<String> = emptyList(),

    var minPlayerQty: Int = 0,

    var maxPlayerQty: Int = 0,

    var desc: String = "",

    var totalRating: Long = 0,

    var ratingQty: Long = 0,

    var createdTime: Long = Calendar.getInstance().timeInMillis,

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

