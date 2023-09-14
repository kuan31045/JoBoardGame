package com.kappstudio.joboardgame.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.*
import kotlinx.coroutines.flow.Flow
import java.util.HashMap

interface JoRepository {

    suspend fun getViewedGame(id: String): Game?

    suspend fun insertViewedGame(game: Game)

    suspend fun updateViewedGame(game: Game)

    fun getAllViewedGames(): LiveData<List<Game>>

    fun getParties(): MutableLiveData<List<Party>>

    fun getParty(id: String): MutableLiveData<Party>

    fun getPartyMsgs(id: String): MutableLiveData<List<PartyMsg>>

    fun getGames(): MutableLiveData<List<Game>>

    fun getGamesByNames(names: List<String>): MutableLiveData<List<Game>>

    fun getUsersByIdList(idList: List<String>): MutableLiveData<List<User>>

    fun getUser(id: String): MutableLiveData<User>

    fun getUserParties(id: String): MutableLiveData<List<Party>>

    fun getUserHosts(id: String): MutableLiveData<List<Party>>

    fun getUserRatings(id: String): MutableLiveData<List<Rating>>

    suspend fun joinParty(id: String): Flow<Result<Boolean>>

    suspend fun leaveParty(id: String): Flow<Result<Boolean>>

    suspend fun insertFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>>

    suspend fun removeFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>>

}