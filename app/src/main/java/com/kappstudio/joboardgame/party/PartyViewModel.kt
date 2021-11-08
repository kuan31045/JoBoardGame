package com.kappstudio.joboardgame.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.map.NavToMapInterface
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
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
        getParties()
    }

    private fun getParties() {
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