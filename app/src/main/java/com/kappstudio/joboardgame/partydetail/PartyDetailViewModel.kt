package com.kappstudio.joboardgame.partydetail


import android.net.Uri
import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.album.NavToAlbumInterFace
import com.kappstudio.joboardgame.user.NavToUserInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class PartyDetailViewModel(private val partyId: String) : ViewModel(), NavToGameDetailInterface,
    NavToUserInterface, NavToAlbumInterFace {


    private var _party: MutableLiveData<Party> = FirebaseService.getLivePartyById(partyId)
    val party: LiveData<Party>
        get() = _party

    private var _partyMsgs: MutableLiveData<List<PartyMsg>> =
        FirebaseService.getLivePartyMsgs(partyId)
    val partyMsgs: LiveData<List<PartyMsg>>
        get() = _partyMsgs

    // nav
    private val _navToGameDetail = MutableLiveData<String?>()
    val navToGameDetail: LiveData<String?>
        get() = _navToGameDetail

    private val _navToUser = MutableLiveData<String?>()
    val navToUser: LiveData<String?>
        get() = _navToUser

    private val _navToAlbum = MutableLiveData<List<String>?>()
    val navToAlbum: LiveData<List<String>?>
        get() = _navToAlbum

    private val _navToMap = MutableLiveData<String?>()
    val navToMap: LiveData<String?>
        get() = _navToMap

    val isJoin: LiveData<Boolean> = Transformations.map(party) {

        it?.playerIdList?.contains(UserManager.user["id"])
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
                    NewPartyMsg(
                        partyId = partyId,
                        msg = newMsg.value ?: ""
                    )
                )

                if (res) {
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
                    is Result.Success -> {
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

    override fun navToGameDetail(gameId: String) {
        if (gameId != "notFound") {
            _navToGameDetail.value = gameId
        } else {
            ToastUtil.show("資料庫內找不到這款遊戲，麻煩您自行去Google")
        }
    }

    override fun onNavToGameDetail() {
        _navToGameDetail.value = null
    }

    override fun navToUser(userId: String) {
        _navToUser.value = userId
    }

    override fun onNavToUser() {
        _navToUser.value = null
    }

    override fun navToAlbum(photo: List<String>) {
        _navToAlbum.value = photo
    }

    override fun onNavToAlbum() {
        _navToAlbum.value = null
    }

    fun navToMap() {
        _navToMap.value = partyId
    }

    fun onNavToMap() {
        _navToMap.value = null
    }
}