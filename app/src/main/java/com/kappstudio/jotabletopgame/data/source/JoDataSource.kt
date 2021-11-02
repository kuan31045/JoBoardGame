package com.kappstudio.jotabletopgame.data.source

import androidx.lifecycle.LiveData
import com.kappstudio.jotabletopgame.data.Game

interface JoDataSource {

    suspend fun getViewedGame(id: String): Game?

    suspend fun insertViewedGame(game: Game)

    suspend fun updateViewedGame(game: Game)

    fun getAllViewedGames(): LiveData<List<Game>>

}