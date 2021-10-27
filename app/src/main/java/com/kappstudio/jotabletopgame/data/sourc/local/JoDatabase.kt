package com.kappstudio.jotabletopgame.data.sourc.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kappstudio.jotabletopgame.data.Game

@Database(entities = [Game::class], version = 1)
abstract class JoDatabase : RoomDatabase() {
    abstract val joDatabaseDao: JoDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: JoDatabase? = null

        fun getDatabase(
            context: Context,
        ): JoDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JoDatabase::class.java,
                    "jo_game_table"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}
