package com.kappstudio.jotabletopgame.party

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.data.sourc.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import com.kappstudio.jotabletopgame.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch

class PartyViewModel : ViewModel(), NavToPartyDetailInterface {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties
    // nav
    private val _navToPartyDetail = MutableLiveData<String?>()
    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail


    init {
         // FirebaseService.addMockParty()
        //  FirebaseService.addMockGame()
        //  FirebaseService.addMockUser()
    }

    init {
        getMyParties()
    }

    private fun getMyParties() {
        viewModelScope.launch {
            _parties  = FirebaseService.getLiveParties()
        }
    }


    override fun navToPartyDetail(partyId: String) {
        _navToPartyDetail.value = partyId
    }

    override fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }

}