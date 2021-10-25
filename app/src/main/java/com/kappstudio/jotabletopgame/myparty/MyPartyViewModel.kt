package com.kappstudio.jotabletopgame.myparty

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.Party
import kotlinx.coroutines.launch

class MyPartyViewModel : ViewModel() {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties


    init {
        getMyParties()
    }

    private fun  getMyParties() {
        viewModelScope.launch {
            _parties.value = FirebaseService.getMyParties()
        }
    }
}