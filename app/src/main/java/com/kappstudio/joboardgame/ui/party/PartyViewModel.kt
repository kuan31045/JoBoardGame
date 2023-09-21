package com.kappstudio.joboardgame.ui.party

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.domain.GetPartiesWithHostUseCase
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface

class PartyViewModel(
    getPartyWithHostUseCase: GetPartiesWithHostUseCase,
) : ViewModel(), NavToPartyDetailInterface {

    val parties: LiveData<Result<List<Party>>> = getPartyWithHostUseCase().asLiveData()
}