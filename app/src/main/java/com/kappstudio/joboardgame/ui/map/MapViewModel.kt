package com.kappstudio.joboardgame.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface

class MapViewModel(
    partyRepository: PartyRepository,
) : ViewModel(), NavToPartyDetailInterface {

    val parties: LiveData<List<Party>> = partyRepository.getParties().asLiveData()
}