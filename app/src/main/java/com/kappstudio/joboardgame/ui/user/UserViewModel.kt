package com.kappstudio.joboardgame.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Report
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch
import java.util.Calendar

class UserViewModel(
    private val userId: String,
    private val userRepository: UserRepository,
) :
    ViewModel(), NavToPartyDetailInterface, NavToUserInterface {

    val user: LiveData<User> = userRepository.getUserStream(userId).asLiveData()

    val me: LiveData<User> = UserManager.user

    private var _parties = MutableLiveData<List<Party>>(mutableListOf())
    val parties: LiveData<List<Party>>
        get() = _parties

    val comingParties: LiveData<List<Party>> = parties.map { parties ->
        parties.filter { party ->
            party.partyTime + 3600000 >= Calendar.getInstance().timeInMillis
        }
    }

    private var _hostParties = MutableLiveData<List<Party>>(mutableListOf())
    val hostParties: LiveData<List<Party>>
        get() = _hostParties

    private var _friendStatus = MutableLiveData<FriendStatus>()
    val friendStatus: LiveData<FriendStatus>
        get() = _friendStatus

    val hasReported = MutableLiveData<Boolean?>()

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


    fun sendFriendRequest() {
        viewModelScope.launch {
            userRepository.sendFriendRequest(userId)
        }
    }

    fun acceptRequest() {
        viewModelScope.launch {
            userRepository.addFriend(userId)
        }
    }

    fun refuseRequest() {
        viewModelScope.launch {
            userRepository.rejectRequest(userId)
        }
    }

    fun reportUser() {
        viewModelScope.launch {
            val report = Report(
                thing = userId,
                violationId = userId
            )

            val result = userRepository.sendReport(report)

            if (result) {
                hasReported.value = true
            }
        }
    }
}