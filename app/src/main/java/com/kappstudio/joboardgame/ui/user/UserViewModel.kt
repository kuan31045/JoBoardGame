package com.kappstudio.joboardgame.ui.user

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch
import java.util.*

class UserViewModel(private val userId: String, private val repository: JoRepository) :
    ViewModel(),
    NavToPartyDetailInterface, NavToUserInterface {
    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user
    private var _hosts = MutableLiveData<List<User>>()
    override val hosts: LiveData<List<User>>
        get() = _hosts
    val me: LiveData<User> = UserManager.user

    private var _parties = MutableLiveData<List<Party>>(mutableListOf())
    val parties: LiveData<List<Party>>
        get() = _parties

    val comingParties: LiveData<List<Party>> = Transformations.map(parties) {
        it.filter {
            it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis
        }
    }

    private var _hostParties = MutableLiveData<List<Party>>(mutableListOf())
    val hostParties: LiveData<List<Party>>
        get() = _hostParties

    private var _friendStatus = MutableLiveData<FriendStatus>()
    val friendStatus: LiveData<FriendStatus>
        get() = _friendStatus

    private var _navToReport = MutableLiveData<User?>()
    val navToReport: LiveData<User?>
        get() = _navToReport

    fun checkFriendStatus() {
        _friendStatus.value = when {
            userId == me.value?.id -> FriendStatus.IS_ME

            user.value?.friendList?.contains(me.value?.id) == true -> FriendStatus.IS_FRIEND

            user.value?.requestList?.contains(me.value?.id) == true -> FriendStatus.SEND_FROM_ME

            me.value?.requestList?.contains(user.value?.id) == true -> FriendStatus.SEND_TO_ME

            else -> FriendStatus.CAN_SEND
        }
    }

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    init {
        getUserInfo()
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            _status.value = LoadApiStatus.LOADING
            _user = repository.getUser(userId)
            //  _parties.value = FirebaseService.getUserParties(userId)
            // _hostParties.value = FirebaseServicegetUserHosts(userId)
            _status.value = LoadApiStatus.DONE
        }
    }

    fun reUser() {
        _user.value = _user.value
    }

    fun sendFriendRequest() {
        FirebaseService.sendFriendRequest(userId)
    }

    fun acceptRequest() {
        FirebaseService.newFriend(userId)
    }

    fun refuseRequest() {
        FirebaseService.refuseRequest(userId)
    }

    fun navToReport() {
        _navToReport.value = user.value
    }

    fun onNavToReport() {
        _navToReport.value = null
    }
}