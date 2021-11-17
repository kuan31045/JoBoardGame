package com.kappstudio.joboardgame.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kappstudio.joboardgame.data.source.local.JoConverters
import kotlinx.parcelize.Parcelize

@Entity(tableName = "jo_game_table")
@TypeConverters(JoConverters::class)
@Parcelize

data class Game(
    @PrimaryKey
    val id: String = "notFound",
    var name: String = "",
    val image: String = "",
    val type: MutableList<String> = mutableListOf(""),
    val time: Int = 0,

    @ColumnInfo(name = "min_player_qty")
    val minPlayerQty: Int = 0,

    @ColumnInfo(name = "max_player_qty")
    val maxPlayerQty: Int = 0,

    val desc: String = "",

    @ColumnInfo(name = "total_rating")
    var totalRating: Long = 0,

    @ColumnInfo(name = "rating_qty")
    var ratingQty: Long = 0,

    @ColumnInfo(name = "created_time")
    var createdTime: Long = 0,

    @ColumnInfo(name = "viewed_time")
    var viewedTime: Long = 0

) : Parcelable

fun toGameMap(game:Game):HashMap<String, Any>{
    return hashMapOf(
        "id" to game.id,
        "name" to game.name,
        "image" to game.image,
        "minPlayerQty" to game.minPlayerQty,
        "maxPlayerQty" to game.maxPlayerQty,
        "time" to game.time
    )
}