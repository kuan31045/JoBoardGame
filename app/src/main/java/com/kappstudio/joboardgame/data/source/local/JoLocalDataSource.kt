package com.kappstudio.joboardgame.data.source.local

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoDataSource
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

    override fun getParties(): MutableLiveData<List<Party>> {
        TODO("Not yet implemented")
    }

    override fun getGames(): MutableLiveData<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun getUsersById(idList: List<String>): MutableLiveData<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getUser(id: String): MutableLiveData<User> {
        TODO("Not yet implemented")
    }

    override fun getUserParties(id: String): MutableLiveData<List<Party>> {
        TODO("Not yet implemented")
    }

    override fun getUserHosts(id: String): MutableLiveData<List<Party>> {
        TODO("Not yet implemented")
    }

    override fun getUserRatings(id: String): MutableLiveData<List<Rating>> {
        TODO("Not yet implemented")
    }

}