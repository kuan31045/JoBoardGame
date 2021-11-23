package com.kappstudio.joboardgame.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.allParties
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import java.util.*


class PartyViewModel : ViewModel(), NavToPartyDetailInterface {

    private var _parties = FirebaseService.getLiveParties()
    val parties: LiveData<List<Party>>
        get() = _parties

  val   connect = FirebaseService.getLiveConnect()


}