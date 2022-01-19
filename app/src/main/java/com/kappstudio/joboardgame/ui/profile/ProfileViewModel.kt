package com.kappstudio.joboardgame.ui.profile

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import com.kappstudio.joboardgame.ui.user.NavToUserInterface
import java.util.*

class ProfileViewModel(private val repository: JoRepository) : ViewModel(),
    NavToPartyDetailInterface,
    NavToGameDetailInterface,
    NavToUserInterface {

    val viewedGames: LiveData<List<Game>> = repository.getAllViewedGames()

    val me: LiveData<User> = UserManager.user

    val parties: LiveData<List<Party>> = repository.getUserParties(me.value?.id ?: "")

    val comingParties: LiveData<List<Party>> = Transformations.map(parties) {
        it.filter {
            it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis
        }
    }

    private var _hosts = MutableLiveData<List<User>>()
    override val hosts: LiveData<List<User>>
        get() = _hosts

    val hostParties: LiveData<List<Party>> =
        repository.getUserHosts(me.value?.id ?: "")

    val status = MutableLiveData(LoadApiStatus.LOADING)

    fun getHosts() {
        val hostIdList = comingParties.value?.map { it.hostId }?.distinctBy { it }
        hostIdList?.let {
            _hosts = repository.getUsersByIdList(it)
        }
    }
}