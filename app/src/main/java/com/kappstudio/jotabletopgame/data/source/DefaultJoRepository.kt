package com.kappstudio.jotabletopgame.data.source

import androidx.lifecycle.LiveData
import com.kappstudio.jotabletopgame.data.Game
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class DefaultJoRepository(
    private val joRemoteDataSource: JoDataSource,
    private val joLocalDataSource: JoDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : JoRepository {
    override suspend fun getViewedGame(id: String): Game? {
        return joLocalDataSource.getViewedGame(id)
    }

    override suspend fun insertViewedGame(game: Game) {
        return joLocalDataSource.insertViewedGame(game)
    }

    override suspend fun updateViewedGame(game: Game) {
        return joLocalDataSource.updateViewedGame(game)
    }

    override fun getAllViewedGames(): LiveData<List<Game>> {
        return joLocalDataSource.getAllViewedGames()
    }


}
