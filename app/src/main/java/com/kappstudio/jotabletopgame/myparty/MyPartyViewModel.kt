package com.kappstudio.jotabletopgame.myparty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.sourc.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import com.kappstudio.jotabletopgame.data.UserManager
import com.kappstudio.jotabletopgame.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch

class MyPartyViewModel : ViewModel(), NavToPartyDetailInterface {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties

    // nav
    private val _navToPartyDetail = MutableLiveData<String?>()
    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail

    init {
    }

    fun getMyParties() {
        viewModelScope.launch {
            _parties.value = FirebaseService.getUserParties(UserManager.user["id"]?:"")
        }
    }

    override fun navToPartyDetail(partyId: String) {
        _navToPartyDetail.value = partyId
    }

    override fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }
}