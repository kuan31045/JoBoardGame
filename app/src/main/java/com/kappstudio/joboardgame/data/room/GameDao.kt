package com.kappstudio.joboardgame.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface GameDao {

    @Upsert
    fun upsert(game: GameEntity)

    @Query("SELECT * FROM game_table ORDER BY viewed_time DESC")
    fun getAllGames(): LiveData<List<GameEntity>>
}