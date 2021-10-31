package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kappstudio.jotabletopgame.data.source.local.JoConverters
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
    val time: Long = 0,

    @ColumnInfo(name = "min_player_qty")
    val minPlayerQty: Int = 0,

    @ColumnInfo(name = "max_player_qty")
    val maxPlayerQty: Int = 0,

    val desc: String = "",

    @ColumnInfo(name = "avg_rating")
    var avgRating: Double = 0.0,

    @ColumnInfo(name = "rating_qty")
    var ratingQty: Long = 0,

    @ColumnInfo(name = "created_time")
    var createdTime: Long = 0,

    @ColumnInfo(name = "viewed_time")
    var viewedTime: Long = 0

) : Parcelable