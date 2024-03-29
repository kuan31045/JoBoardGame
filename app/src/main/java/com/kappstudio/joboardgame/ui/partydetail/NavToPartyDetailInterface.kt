package com.kappstudio.joboardgame.ui.partydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface NavToPartyDetailInterface {

    companion object {
        private val _navToPartyDetail = MutableLiveData<String?>()
    }

    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail

    fun navToPartyDetail(partyId: String) {
        _navToPartyDetail.value = partyId
    }

    fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }
}