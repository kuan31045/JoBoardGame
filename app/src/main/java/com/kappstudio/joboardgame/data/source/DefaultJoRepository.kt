package com.kappstudio.joboardgame.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.User
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

    override fun getParties(): MutableLiveData<List<Party>> {
        return joRemoteDataSource.getParties()
    }

    override fun getGames(): MutableLiveData<List<Game>> {
        return joRemoteDataSource.getGames()
    }

    override fun getUsersById(idList: List<String>): MutableLiveData<List<User>> {
        return joRemoteDataSource.getUsersById(idList)
    }

    override fun getUser(id: String): MutableLiveData<User> {
        return joRemoteDataSource.getUser(id)
    }

    override fun getUserParties(id:String): MutableLiveData<List<Party>> {
        return joRemoteDataSource.getUserParties(id)
    }

    override fun getUserHosts(id: String): MutableLiveData<List<Party>> {
        return joRemoteDataSource.getUserHosts(id)
    }

    override fun getUserRatings(id: String): MutableLiveData<List<Rating>> {
        return joRemoteDataSource.getUserRatings(id)
    }

}
