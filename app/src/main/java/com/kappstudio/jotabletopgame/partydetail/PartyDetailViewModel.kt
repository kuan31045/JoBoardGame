package com.kappstudio.jotabletopgame.partydetail

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import com.kappstudio.jotabletopgame.data.User
import com.kappstudio.jotabletopgame.data.UserObject
import kotlinx.coroutines.launch
import timber.log.Timber

class PartyDetailViewModel(private val partyId: String) : ViewModel() {
    val party: LiveData<Party?> = FirebaseService.getPartyById(partyId)

    private var _host = MutableLiveData<User?>()
    val host: LiveData<User?>
        get() = _host


    val isJoin: LiveData<Boolean> = Transformations.map(party) {
        it?.playerIdList?.contains(UserObject.mUserId)
    }

    val playerQtyStatus: LiveData<String> = Transformations.map(party) {
        var str = "${it?.playerIdList?.size}/${it?.requirePlayerQty}"
        if (it?.playerIdList?.size ?: 0 < it?.requirePlayerQty ?: 0) {
            str += appInstance.getString(R.string.lack)
        }
        str
    }

    init {

    }

    fun setHost() {
        viewModelScope.launch {
            _host.value = FirebaseService.getUserById("user1").value
            Timber.d("set host${host.value}")

        }

    }

    fun joinParty() {
        FirebaseService.joinParty(partyId)
    }

    fun leaveParty() {
        FirebaseService.leaveParty(partyId)
    }


}