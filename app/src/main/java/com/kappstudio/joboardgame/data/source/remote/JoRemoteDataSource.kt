package com.kappstudio.joboardgame.data.source.remote

import androidx.lifecycle.LiveData
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.source.JoDataSource

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