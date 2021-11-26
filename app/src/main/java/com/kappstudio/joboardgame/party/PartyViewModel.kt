package com.kappstudio.joboardgame.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface


class PartyViewModel : ViewModel(), NavToPartyDetailInterface {
     private var _parties = FirebaseService.getLiveParties()
    val parties: LiveData<List<Party>>
        get() = _parties




}