package com.kappstudio.joboardgame.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch

class PartyViewModel : ViewModel(), NavToPartyDetailInterface {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties

    init {
        getParties()
    }

    private fun getParties() {
        viewModelScope.launch {
            _parties  = FirebaseService.getLiveParties()
        }
    }



}