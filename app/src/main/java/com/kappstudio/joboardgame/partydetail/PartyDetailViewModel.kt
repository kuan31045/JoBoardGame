package com.kappstudio.joboardgame.partydetail


import android.net.Uri
import androidx.lifecycle.*
import com.kappstudio.joboardgame.*
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.user.NavToUserInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class PartyDetailViewModel(private val partyId: String) : ViewModel(), NavToGameDetailInterface,
    NavToUserInterface {

    private var _host = MutableLiveData<User>()
    val host: LiveData<User>
        get() = _host

    private var _partyGames = MutableLiveData<List<Game>>(mutableListOf())
    val partyGames: LiveData<List<Game>>
        get() = _partyGames

    private var _partyUsers = MutableLiveData<List<User>>(mutableListOf())
    val partyUsers: LiveData<List<User>>
        get() = _partyUsers

    private val _isSend = MutableLiveData(false)
    val isSend: LiveData<Boolean>
        get() = _isSend

    private var _party: MutableLiveData<Party> = FirebaseService.getLivePartyById(partyId)
    val party: LiveData<Party>
        get() = _party

    private var _partyMsgs: MutableLiveData<List<PartyMsg>> =
        FirebaseService.getLivePartyMsgs(partyId)
    val partyMsgs: LiveData<List<PartyMsg>>
        get() = _partyMsgs

    private val _reportOk = MutableLiveData<Boolean?>()
    val reportOk: LiveData<Boolean?>
        get() = _reportOk

    val isJoin: LiveData<Boolean> = Transformations.map(party) {
        it?.playerIdList?.contains(UserManager.user.value?.id ?: "")
    }

    val playerQtyStatus: LiveData<String> = Transformations.map(party) {
        var str = "${it?.playerIdList?.size}/${it?.requirePlayerQty}"
        if (it?.playerIdList?.size ?: 0 < it?.requirePlayerQty ?: 0) {
            str += appInstance.getString(R.string.lack)
        }
        str
    }

    //Msg edittext
    var newMsg = MutableLiveData("")

    private val _status = MutableLiveData<LoadApiStatus>()
    val status: LiveData<LoadApiStatus>
        get() = _status

    fun joinParty() {
        FirebaseService.joinParty(partyId)
    }

    fun leaveParty() {
        FirebaseService.leaveParty(partyId)

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
            ToastUtil.show("請填寫內容!")
        }

    }

    fun uploadPhoto(fileUri: Uri?) {
        viewModelScope.launch {
            fileUri?.let {

                _status.value = LoadApiStatus.LOADING

                when (val result = FirebaseService.uploadPhoto(it)) {
                    is com.kappstudio.joboardgame.data.Resource.Success -> {
                        val res = result.data
                        Timber.d("Photo: $res")
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
                is com.kappstudio.joboardgame.data.Resource.Success -> {
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

    fun setGames() {
        val list = mutableListOf<Game>()
        party.value?.gameNameList?.forEach { name ->
            val query = allGames.value?.filter { game ->
                game.name == name
            }
            if (query != null && query.isNotEmpty()) {
                list.add(query.first())
            } else {
                list.add(
                    Game(
                        id = "notFound",
                        name = name
                    )
                )
            }
        }
        _partyGames.value = list
    }

    fun setUsers() {

        _partyUsers.value =
            allUsers.value?.filter { party.value?.playerIdList?.contains(it.id) == true }


    }

    fun setHost() {
        allUsers.value?.first { it.id == party.value?.hostId ?: "" }?.let {
            _host.value = it
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
        _reportOk.value =null
    }
}