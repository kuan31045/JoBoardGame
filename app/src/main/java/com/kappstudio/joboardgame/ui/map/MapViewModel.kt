package com.kappstudio.joboardgame.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapViewModel(
    private val partyRepository: PartyRepository,
) : ViewModel(), NavToPartyDetailInterface {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>> = _parties

    init {
        getParties()
    }

    private fun getParties() {
        viewModelScope.launch {
            _parties.value = partyRepository.getParties().first().reversed()
        }
    }
}