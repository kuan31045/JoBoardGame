package com.kappstudio.joboardgame.profile

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
import com.kappstudio.joboardgame.user.FriendStatus
import com.kappstudio.joboardgame.user.NavToUserInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import java.util.*

class ProfileViewModel(joRepository: JoRepository) : ViewModel(), NavToGameDetailInterface,
    NavToPartyDetailInterface, NavToUserInterface {
    val viewedGames: LiveData<List<Game>> = joRepository.getAllViewedGames()

    val me: LiveData<User> = UserManager.user

    private var _parties = MutableLiveData<List<Party>>(mutableListOf())
    val parties: LiveData<List<Party>>
        get() = _parties
    val comingParties:LiveData<List<Party>> = Transformations.map(parties){
        it.filter {
            it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis
        }
    }
    private var _hostParties = MutableLiveData<List<Party>>(mutableListOf())
    val hostParties: LiveData<List<Party>>
        get() = _hostParties


    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    init {
        getUserInfo()
    }

     fun getUserInfo() {
        viewModelScope.launch {
            _status.value = LoadApiStatus.LOADING

            me.value?.let {
                _parties.value = FirebaseService.getUserParties(it.id)
                _hostParties.value = FirebaseService.getUserHosts(it.id)
            }

            _status.value = LoadApiStatus.DONE
         }
    }

}