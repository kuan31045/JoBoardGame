package com.kappstudio.joboardgame.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import java.util.HashMap

class JoRepositoryImpl(
    private val joRemoteDataSource: JoDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : JoRepository {

    override fun getParties(): MutableLiveData<List<Party>> {
        return joRemoteDataSource.getParties()
    }

    override fun getParty(id: String): MutableLiveData<Party> {
        return joRemoteDataSource.getParty(id)
    }

    override fun getPartyMsgs(id: String): MutableLiveData<List<PartyMsg>> {
        return joRemoteDataSource.getPartyMsgs(id)
    }

    override fun getGames(): MutableLiveData<List<Game>> {
        return joRemoteDataSource.getGames()
    }

    override fun getGamesByNames(names: List<String>): MutableLiveData<List<Game>> {
        return joRemoteDataSource.getGamesByNames(names)
    }

    override fun getUsersByIdList(idList: List<String>): MutableLiveData<List<User>> {
        return joRemoteDataSource.getUsersByIdList(idList)
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

    override suspend fun joinParty(id: String): Flow<Result<Boolean>> {
        return joRemoteDataSource.joinParty(id)
    }

    override suspend fun leaveParty(id: String): Flow<Result<Boolean>> {
        return joRemoteDataSource.leaveParty(id)
    }

    override suspend fun insertFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>> {
        return joRemoteDataSource.insertFavorite(gameMap)
    }

    override suspend fun removeFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>> {
        return joRemoteDataSource.removeFavorite(gameMap)
    }

}
