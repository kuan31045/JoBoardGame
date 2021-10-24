package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Game(
    val id: String = "notFound",
    var name: String = "",
    val image: String = "",
    val type: MutableList<String>? = null,
    val time: Long = 0,
    val minPlayerQty: Int = 0,
    val maxPlayerQty: Int = 0,
    val desc: String = "",
    var avgRating: Double = 0.0,
    var ratingQty: Long = 0,
    var createdTime: Long = 0,
) : Parcelable {
    fun toMap(): HashMap<String, String> {
        return hashMapOf(
            "id" to id,
            "name" to name,
            "image" to image
        )
    }
}