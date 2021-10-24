package com.kappstudio.jotabletopgame.partydetail


import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.data.*
import kotlinx.coroutines.launch
import timber.log.Timber

class PartyDetailViewModel(private val partyId: String) : ViewModel() {
    val party: LiveData<Party?> = FirebaseService.getLivePartyById(partyId)

    private var _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games

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


}