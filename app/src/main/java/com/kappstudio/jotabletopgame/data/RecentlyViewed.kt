package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "jotabletopgame_game_table")
data class RecentlyViewed(
    @PrimaryKey
    var id: String = "",

    var name: String = "",

    var image: String = "",

    @ColumnInfo(name = "viewed_time")
    var viewedTime: Long = 0,

    )
