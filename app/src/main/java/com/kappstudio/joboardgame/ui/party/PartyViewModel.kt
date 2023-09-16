package com.kappstudio.joboardgame.ui.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.domain.GetPartyWithHostUseCase
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface

class PartyViewModel(
    getPartyWithHostUseCase: GetPartyWithHostUseCase,
) : ViewModel(), NavToPartyDetailInterface {

    val parties: LiveData<Result<List<Party>>> = getPartyWithHostUseCase().asLiveData()
}

