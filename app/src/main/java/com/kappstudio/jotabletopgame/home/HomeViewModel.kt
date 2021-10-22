package com.kappstudio.jotabletopgame.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import timber.log.Timber

class HomeViewModel : ViewModel() {

    val parties: LiveData<List<Party>?> = FirebaseService.getAllParties()

    // nav
    private val _navToPartyDetail = MutableLiveData<String?>()
    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail

    init {
        //   FirebaseService.addMockParty()
    }

    fun navToPartyDetail(partyId:String) {
        _navToPartyDetail.value = partyId
    }

    fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }


}