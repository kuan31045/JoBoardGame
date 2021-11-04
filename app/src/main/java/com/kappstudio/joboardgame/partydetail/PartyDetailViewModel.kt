package com.kappstudio.joboardgame.partydetail


import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.user.NavToUserInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class PartyDetailViewModel(private val partyId: String) : ViewModel(), NavToGameDetailInterface,
    NavToUserInterface {


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

    // nav
    private val _navToUser = MutableLiveData<String?>()
    val navToUser: LiveData<String?>
        get() = _navToUser

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


    fun joinParty() {
        FirebaseService.joinParty(partyId)
    }

    fun leaveParty() {
        FirebaseService.leaveParty(partyId)
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
}