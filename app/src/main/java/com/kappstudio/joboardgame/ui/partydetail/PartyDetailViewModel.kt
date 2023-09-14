package com.kappstudio.joboardgame.ui.partydetail

import android.net.Uri
import androidx.lifecycle.*
import com.kappstudio.joboardgame.*
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.remote.FirebaseService
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.ui.user.NavToUserInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.kappstudio.joboardgame.util.ToastUtil

class PartyDetailViewModel(
    private val partyId: String,
    private val repository: JoRepository,
) : ViewModel(), NavToGameDetailInterface,
    NavToUserInterface {

    val party: LiveData<Party> = repository.getParty(partyId)

    private var _host = MutableLiveData<User>()
    val host: LiveData<User>
        get() = _host

    private var _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games

    private var _players = MutableLiveData<List<User>>()
    val players: LiveData<List<User>>
        get() = _players

    private val _isSend = MutableLiveData(false)
    val isSend: LiveData<Boolean>
        get() = _isSend

    val partyMsgs: LiveData<List<PartyMsg>> = repository.getPartyMsgs(partyId)

    private val _reportOk = MutableLiveData<Boolean?>()
    val reportOk: LiveData<Boolean?>
        get() = _reportOk

    val isJoin = party.map {
        it.playerIdList.contains(UserManager.user.value?.id ?: "")
    }

    val playerQtyStatus = party.map {
        "${it.playerIdList.size}/${it.requirePlayerQty}"
    }

    //Msg edittext
    var newMsg = MutableLiveData("")

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun joinParty() {
        coroutineScope.launch {
            repository.joinParty(partyId).collect {
                if (it is Result.Success) {
                    ToastUtil.show(appInstance.getString(R.string.welcome))
                }
            }
        }
    }

    fun leaveParty() {
        coroutineScope.launch {
            repository.leaveParty(partyId).collect {
                if (it is Result.Success) {
                    ToastUtil.show(appInstance.getString(R.string.bye))
                }
            }
        }
    }

    fun sendMsg() {
        if (newMsg.value?.replace("\\s".toRegex(), "") != "") {
            viewModelScope.launch {
                val res = FirebaseService.sendPartyMsg(
                    PartyMsg(
                        partyId = partyId,
                        msg = newMsg.value ?: ""
                    )
                )

                if (res) {
                    _isSend.value = true
                    ToastUtil.show(appInstance.getString(R.string.send_ok))
                    newMsg.value = ""
                }
            }
        } else {
            ToastUtil.show(appInstance.getString(R.string.enter_content))
        }
    }

    fun uploadPhoto(fileUri: Uri?) {
        viewModelScope.launch {
            fileUri?.let {
                _status.value = LoadApiStatus.LOADING

                when (val result = FirebaseService.uploadPhoto(it)) {
                    is Result.Success -> {
                        val res = result.data
                        addPartyPhoto(res)
                    }

                    else -> {
                        ToastUtil.show(appInstance.getString(R.string.upload_fail))
                        _status.value = LoadApiStatus.ERROR
                    }
                }
            }
        }
    }

    private suspend fun addPartyPhoto(photo: String) {
        viewModelScope.launch {
            when (FirebaseService.addPartyPhoto(partyId, photo)) {
                is Result.Success -> {
                    ToastUtil.show(appInstance.getString(R.string.upload_ok))
                    _status.value = LoadApiStatus.DONE
                }

                else -> {
                    ToastUtil.show(appInstance.getString(R.string.upload_fail))
                    _status.value = LoadApiStatus.ERROR
                }
            }
        }
    }

    fun getHostUser() {
        party.value?.let {
            _host = repository.getUser(it.hostId)
        }
    }

    fun getGames() {
        party.value?.let {
            _games = repository.getGamesByNames(it.gameNameList)
        }
    }

    fun getPlayers() {
        party.value?.let {
            if (it.playerIdList.isNotEmpty()) {
                _players = repository.getUsersByIdList(it.playerIdList)
            } else {
                _players.value = listOf()
            }
        }
    }

    fun reportMsg(msg: PartyMsg) {
        viewModelScope.launch {
            val report = Report(
                thing = "${msg.userId}: ${msg.msg}",
                violationId = msg.id
            )
            val result = FirebaseService.sendReport(report)
            if (result) {
                _reportOk.value = true
            }
        }
    }

    fun onReportOk() {
        _reportOk.value = null
    }
}