package com.kappstudio.joboardgame.ui.partydetail

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.PartyMsg
import com.kappstudio.joboardgame.data.Report
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.ui.user.NavToUserInterface
import com.kappstudio.joboardgame.util.ToastUtil
import com.kappstudio.joboardgame.util.checkValid
import kotlinx.coroutines.launch

class PartyDetailViewModel(
    private val partyId: String,
    private val partyRepository: PartyRepository,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) : ViewModel(), NavToGameDetailInterface,
    NavToUserInterface {

    val party: LiveData<Party> = partyRepository.getParty(partyId).asLiveData()

    private var _host = MutableLiveData<User>()
    val host: LiveData<User> = _host

    lateinit var games: LiveData<List<Game>>
        private set

    private var _players = MutableLiveData<List<User>>()
    val players: LiveData<List<User>> = _players

    private val _isSend = MutableLiveData(false)
    val isSend: LiveData<Boolean> = _isSend

    val partyMsgs: LiveData<List<PartyMsg>> = partyRepository.getPartyMsgs(partyId).asLiveData()

    private val _reportOk = MutableLiveData<Boolean>()
    val reportOk: LiveData<Boolean> = _reportOk

    val isJoin = party.map {
        it.playerIdList.contains(UserManager.user.value?.id ?: "")
    }

    val playerQtyStatus = party.map {
        "${it.playerIdList.size}/${it.requirePlayerQty}"
    }

    //Msg edittext
    var newMsg = MutableLiveData("")

    val status = MutableLiveData<LoadApiStatus>()

    private val _toastMsgRes = MutableLiveData<Int>()
    val toastMsgRes: LiveData<Int> = _toastMsgRes

    fun joinParty() {
        viewModelScope.launch {
            partyRepository.joinParty(partyId)
        }
    }

    fun leaveParty() {
        viewModelScope.launch {
            partyRepository.leaveParty(partyId)
        }
    }

    fun sendMsg() {
        if (!newMsg.value.checkValid()) {
            _toastMsgRes.value = R.string.cant_empty
            return
        }

        viewModelScope.launch {
            val res = partyRepository.sendPartyMsg(
                PartyMsg(
                    partyId = partyId,
                    msg = newMsg.value ?: ""
                )
            )

            if (res) {
                _isSend.value = true
                newMsg.value = ""
            }
        }
    }

    fun uploadPhoto(fileUri: Uri) {
        status.value =  LoadApiStatus.LOADING

        viewModelScope.launch {
            partyRepository.addPartyPhoto(partyId, fileUri).collect { result ->
                status.value = when (result) {
                    is Result.Loading -> LoadApiStatus.LOADING
                    is Result.Success -> {
                        ToastUtil.show(result.data)
                        LoadApiStatus.DONE
                    }
                    else -> {
                        ToastUtil.show("failed")
                        LoadApiStatus.ERROR
                    }
                }
            }
        }
    }

    fun getHostUser() {
        viewModelScope.launch {
            party.value?.let {
                _host.value = userRepository.getUser(it.hostId)
            }
        }
    }

    fun getGames() {
        party.value?.let {
            games = gameRepository.getGamesByNames(it.gameNameList).asLiveData()
        }
    }

    fun getPlayers() {
        if (party.value!!.playerIdList.isEmpty()) {
            return
        }
        viewModelScope.launch {
            val result = userRepository.getUsersByIdList(party.value!!.playerIdList)
            if (result is Result.Success) {
                _players.value = result.data ?: emptyList()
            }
        }
    }

    fun deleteMsg(id: String) {
        viewModelScope.launch {
            partyRepository.deletePartyMsg(id)
        }
    }

    fun reportMsg(msg: PartyMsg) {
        viewModelScope.launch {
            val report = Report(
                thing = "${msg.userId}: ${msg.msg}",
                violationId = msg.id
            )

            val result = partyRepository.sendReport(report)

            if (result) {
                _reportOk.value = true
            }
        }
    }
}