package com.kappstudio.jotabletopgame.party

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party

class PartyViewModel : ViewModel() {

    val parties: LiveData<List<Party>> = FirebaseService.getLiveParties()

    // nav
    private val _navToPartyDetail = MutableLiveData<String?>()
    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail

    init {
         // FirebaseService.addMockParty()
        //  FirebaseService.addMockGame()
        //  FirebaseService.addMockUser()
    }

    fun navToPartyDetail(partyId: String) {
        _navToPartyDetail.value = partyId
    }

    fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }

}