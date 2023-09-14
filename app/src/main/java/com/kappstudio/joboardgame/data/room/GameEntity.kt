package com.kappstudio.joboardgame.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kappstudio.joboardgame.data.Game

@Entity(tableName = "game_table")
class GameEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val image: String,
    @ColumnInfo(name = "viewed_time")
    val viewedTime: Long,
)

fun GameEntity.toGame() = Game(
    id = id,
    name = name,
    image = image
)