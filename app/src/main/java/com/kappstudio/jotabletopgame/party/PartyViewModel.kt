package com.kappstudio.jotabletopgame.party

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import com.kappstudio.jotabletopgame.data.UserManager
import com.kappstudio.jotabletopgame.partydetail.NavToPartyDetailInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PartyViewModel : ViewModel(), NavToPartyDetailInterface {

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties
    // nav
    private val _navToPartyDetail = MutableLiveData<String?>()
    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail
    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = Job()

    // the Coroutine runs using the Main (UI) dispatcher
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
         // FirebaseService.addMockParty()
        //  FirebaseService.addMockGame()
        //  FirebaseService.addMockUser()
    }

    init {
        getMyParties()
    }

    private fun getMyParties() {
        coroutineScope.launch {
            _parties  = FirebaseService.getLiveParties()
        }
    }


    override fun navToPartyDetail(partyId: String) {
        _navToPartyDetail.value = partyId
    }

    override fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }
    override fun onCleared() {
        super.onCleared()
        viewModelJob.start()
    }

}