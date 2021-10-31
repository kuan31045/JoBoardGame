package com.kappstudio.jotabletopgame.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kappstudio.jotabletopgame.data.Game

@Dao
interface JoDatabaseDao {
    @Insert
    suspend fun insert(game: Game)

    @Update
    suspend fun update(game: Game)

    @Query("SELECT * FROM jo_game_table ORDER BY viewed_time DESC")
    fun getAllGames(): LiveData<List<Game>>

    @Query("SELECT * FROM jo_game_table WHERE id=:key")
    suspend fun get(key: String): Game?

}