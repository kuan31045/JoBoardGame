package com.kappstudio.joboardgame.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kappstudio.joboardgame.data.source.room.Converters
import kotlinx.parcelize.Parcelize
import java.util.*
import kotlin.collections.HashMap

@Entity(tableName = "jo_game_table")
@TypeConverters(Converters::class)
@Parcelize
data class Game(
    @PrimaryKey
    var id: String = "notFound",
    var name: String = "",
    var image: String = "",
    var type: MutableList<String> = mutableListOf(""),
    var time: Int = 0,
    var tools: List<String> = emptyList(),

    @ColumnInfo(name = "min_player_qty")
    var minPlayerQty: Int = 0,

    @ColumnInfo(name = "max_player_qty")
    var maxPlayerQty: Int = 0,

    var desc: String = "",

    @ColumnInfo(name = "total_rating")
    var totalRating: Long = 0,

    @ColumnInfo(name = "rating_qty")
    var ratingQty: Long = 0,

    @ColumnInfo(name = "created_time")
    var createdTime: Long = Calendar.getInstance().timeInMillis,

    @ColumnInfo(name = "viewed_time")
    var viewedTime: Long = 0

) : Parcelable

fun toGameMap(game: Game): HashMap<String, Any> {
    return hashMapOf(
        "id" to game.id,
        "name" to game.name,
        "image" to game.image,
        "minPlayerQty" to game.minPlayerQty,
        "maxPlayerQty" to game.maxPlayerQty,
        "time" to game.time
    )
}