package com.kappstudio.jotabletopgame.home

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {

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