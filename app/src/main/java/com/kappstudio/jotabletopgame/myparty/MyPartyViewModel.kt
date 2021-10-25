package com.kappstudio.jotabletopgame.myparty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.Party
import kotlinx.coroutines.launch

class MyPartyViewModel : ViewModel() {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties

    // nav
    private val _navToPartyDetail = MutableLiveData<String?>()
    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail

    init {
        getMyParties()
    }

    fun getMyParties() {
        viewModelScope.launch {
            _parties.value = FirebaseService.getMyParties()
        }
    }

    fun navToPartyDetail(partyId: String) {
        _navToPartyDetail.value = partyId
    }

    fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }
}