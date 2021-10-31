package com.kappstudio.jotabletopgame.data.source.remote

import androidx.lifecycle.LiveData
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.source.JoDataSource

object JoRemoteDataSource: JoDataSource {
    override suspend fun getViewedGame(id: String): Game? {
        TODO("Not yet implemented")
    }

    override suspend fun insertViewedGame(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun updateViewedGame(game: Game) {
        TODO("Not yet implemented")
    }

    override fun getAllViewedGames(): LiveData<List<Game>> {
        TODO("Not yet implemented")
    }


}