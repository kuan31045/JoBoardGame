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
import com.kappstudio.joboardgame.data.repository.StorageRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.domain.GetPartyMsgsUseCase
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.ui.user.NavToUserInterface
import com.kappstudio.joboardgame.util.ToastUtil
import com.kappstudio.joboardgame.util.checkValid
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PartyDetailViewModel(
    private val partyId: String,
    private val partyRepository: PartyRepository,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
    getPartyMsgsUseCase: GetPartyMsgsUseCase,
) : ViewModel(), NavToGameDetailInterface,
    NavToUserInterface {

    val party: LiveData<Party> = partyRepository.getPartyStream(partyId).asLiveData()

    private var _host = MutableLiveData<User>()
    val host: LiveData<User> = _host

    private var _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>> = _games

    private var _players = MutableLiveData<List<User>>()
    val players: LiveData<List<User>> = _players

    private val _isSend = MutableLiveData(false)
    val isSend: LiveData<Boolean> = _isSend

    val partyMsgs: LiveData<Result<List<PartyMsg>>> = getPartyMsgsUseCase(partyId).asLiveData()

    val hasReported = MutableLiveData<Boolean?>()

    val isJoin = party.map {
        it.playerIdList.contains(UserManager.user.value?.id ?: "")
    }

    val playerQtyStatus = party.map {
        "${it.playerIdList.size}/${it.requirePlayerQty}"
    }

    //Msg edittext
    var newMsg = MutableLiveData("")

    val status = MutableLiveData<LoadApiStatus>()

    fun setupParty() {
        getHostUser()
        getGames()
        getPlayers()
    }

    private fun getHostUser() {
        viewModelScope.launch {
            party.value?.let {
                _host.value = userRepository.getUser(it.hostId)
            }
        }
    }

    private fun getGames() {
        viewModelScope.launch {
            val result = gameRepository.getGamesByNames(party.value!!.gameNameList)
            _games.value = result
        }
    }

    private fun getPlayers() {
        viewModelScope.launch {
            val result = userRepository.getUsersByIdList(party.value!!.playerIdList)
            if (result is Result.Success) {
                _players.value = result.data ?: emptyList()
            }
        }
    }

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
            ToastUtil.showByRes((R.string.cant_empty))
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
        viewModelScope.launch {
            storageRepository.uploadPhoto(fileUri).collect { result ->
                status.value = when (result) {
                    is Result.Success -> {
                        val photo = result.data
                        partyRepository.addPartyPhoto(partyId, photo)
                        userRepository.addMyPhoto(photo)
                        ToastUtil.showByRes(R.string.upload_ok)
                        LoadApiStatus.DONE
                    }

                    is Result.Fail -> {
                        ToastUtil.showByRes(result.stringRes)
                        if (result.stringRes == R.string.check_internet) {
                            LoadApiStatus.LOADING
                        } else {
                            LoadApiStatus.ERROR
                        }
                    }

                    else -> LoadApiStatus.LOADING
                }
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

            val result = userRepository.sendReport(report)

            if (result) {
                hasReported.value = true
            }
        }
    }
}