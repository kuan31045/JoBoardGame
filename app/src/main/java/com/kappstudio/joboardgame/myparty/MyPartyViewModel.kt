package com.kappstudio.joboardgame.myparty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.UserManager
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.launch

class MyPartyViewModel : ViewModel(), NavToPartyDetailInterface {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties



    init {
    }

    fun getMyParties() {
        viewModelScope.launch {
            _parties.value = FirebaseService.getUserParties(UserManager.user["id"]?:"")
        }
    }
}