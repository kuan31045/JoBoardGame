package com.kappstudio.joboardgame.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.User

interface JoRepository {

    suspend fun getViewedGame(id: String): Game?

    suspend fun insertViewedGame(game: Game)

    suspend fun updateViewedGame(game: Game)

    fun getAllViewedGames(): LiveData<List<Game>>

    fun getParties(): MutableLiveData<List<Party>>

    fun getGames(): MutableLiveData<List<Game>>

    fun getUsersById(idList: List<String>): MutableLiveData<List<User>>

    fun getUser(id: String): MutableLiveData<User>

    fun getUserParties(id: String): MutableLiveData<List<Party>>

    fun getUserHosts(id: String): MutableLiveData<List<Party>>

    fun getUserRatings(id: String): MutableLiveData<List<Rating>>

}