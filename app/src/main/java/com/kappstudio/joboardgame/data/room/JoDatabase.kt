package com.kappstudio.joboardgame.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class], version = 1)
abstract class JoDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}