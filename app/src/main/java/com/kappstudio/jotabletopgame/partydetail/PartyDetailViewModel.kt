package com.kappstudio.jotabletopgame.partydetail


import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.data.*
import com.kappstudio.jotabletopgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class PartyDetailViewModel(private val partyId: String) : ViewModel(), NavToGameDetailInterface {

    private var _party: MutableLiveData<Party> =  FirebaseService.getLivePartyById(partyId)
    val party: LiveData<Party>
        get() = _party

    private var _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games

    // nav
    private val _navToGameDetail = MutableLiveData<String?>()
    val navToGameDetail: LiveData<String?>
        get() = _navToGameDetail

    val isJoin: LiveData<Boolean> = Transformations.map(party) {
        setGame()

        it?.playerIdList?.contains(UserManager.user["id"])
    }

    val playerQtyStatus: LiveData<String> = Transformations.map(party) {
        var str = "${it?.playerIdList?.size}/${it?.requirePlayerQty}"
        if (it?.playerIdList?.size ?: 0 < it?.requirePlayerQty ?: 0) {
            str += appInstance.getString(R.string.lack)
        }
        str
    }

    init {
        //  getParty()
    }

    private fun getParty() {
        viewModelScope.launch {
            _party = FirebaseService.getLivePartyById(partyId)
        }
    }

    private fun setGame() {
        viewModelScope.launch {
            Timber.d("setGame")
            _games.value =
                FirebaseService.getGamesByNames(party.value?.gameNameList ?: mutableListOf())
        }
    }


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


}