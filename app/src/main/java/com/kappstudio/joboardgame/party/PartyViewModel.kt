package com.kappstudio.joboardgame.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch
import java.util.*

class PartyViewModel : ViewModel(), NavToPartyDetailInterface {

    private var _parties = FirebaseService.getLiveParties()
    val parties : LiveData<List<Party>>
        get() = _parties


    val openParties: LiveData<List<Party>> = Transformations.map(parties) {
        it.filter { it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis }
    }

    val overParties: LiveData<List<Party>> = Transformations.map(parties) {
        it.filter { it.partyTime + 3600000 < Calendar.getInstance().timeInMillis }
    }


}