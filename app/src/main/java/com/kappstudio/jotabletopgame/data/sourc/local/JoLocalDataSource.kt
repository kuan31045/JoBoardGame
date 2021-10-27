package com.kappstudio.jotabletopgame.data.sourc.local

import android.content.Context
import androidx.lifecycle.LiveData
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.sourc.JoDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JoLocalDataSource(
    val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :JoDataSource{
    override suspend fun getViewedGame(id: String): Game? = withContext(ioDispatcher) {
       JoDatabase.getDatabase(context).joDatabaseDao.get(id)
    }

    override suspend fun insertViewedGame(game: Game) = withContext(ioDispatcher) {
        JoDatabase.getDatabase(context).joDatabaseDao.insert(game)
    }

    override suspend fun updateViewedGame(game: Game) = withContext(ioDispatcher) {
        JoDatabase.getDatabase(context).joDatabaseDao.update(game)
    }

    override  fun getAllViewedGames(): LiveData<List<Game>> {
      return  JoDatabase.getDatabase(context).joDatabaseDao.getAllGames()
    }


}