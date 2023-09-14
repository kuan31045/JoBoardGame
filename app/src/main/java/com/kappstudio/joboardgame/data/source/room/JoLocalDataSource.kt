package com.kappstudio.joboardgame.data.source.room

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.source.JoDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.HashMap

class JoLocalDataSource(
    val context: Context,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :JoDataSource{

    override suspend fun getViewedGame(id: String): Game? = withContext(ioDispatcher) {
       JoDatabase.getDatabase(context).gameDao.get(id)
    }

    override suspend fun insertViewedGame(game: Game) = withContext(ioDispatcher) {
        JoDatabase.getDatabase(context).gameDao.insert(game)
    }

    override suspend fun updateViewedGame(game: Game) = withContext(ioDispatcher) {
        JoDatabase.getDatabase(context).gameDao.update(game)
    }

    override  fun getAllViewedGames(): LiveData<List<Game>> {
      return  JoDatabase.getDatabase(context).gameDao.getAllGames()
    }

    override fun getParties(): MutableLiveData<List<Party>> {
        TODO("Not yet implemented")
    }

    override fun getParty(id: String): MutableLiveData<Party> {
        TODO("Not yet implemented")
    }

    override fun getPartyMsgs(id: String): MutableLiveData<List<PartyMsg>> {
        TODO("Not yet implemented")
    }

    override fun getGames(): MutableLiveData<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun getGamesByNames(names: List<String>): MutableLiveData<List<Game>> {
        TODO("Not yet implemented")
    }

    override fun getUsersByIdList(idList: List<String>): MutableLiveData<List<User>> {
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

    override suspend fun joinParty(id: String): Flow<Result<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun leaveParty(id: String): Flow<Result<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>> {
        TODO("Not yet implemented")
    }

}