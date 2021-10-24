package com.kappstudio.jotabletopgame.home

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel : ViewModel() {

    private var partiesResult = FirebaseService.getLiveParties()
    val canSetHostName: LiveData<Boolean> = Transformations.map(partiesResult) {
        it?.let {
            setHostName()
        }

        it.isNotEmpty()
    }

    private var _parties = MutableLiveData<List<Party>>()
    val parties: LiveData<List<Party>>
        get() = _parties


    // nav
    private val _navToPartyDetail = MutableLiveData<String?>()
    val navToPartyDetail: LiveData<String?>
        get() = _navToPartyDetail

    init {
        //  FirebaseService.addMockParty()
        //  FirebaseService.addMockGame()
        //  FirebaseService.addMockUser()
    }

    fun navToPartyDetail(partyId: String) {
        _navToPartyDetail.value = partyId
    }

    fun onNavToPartyDetail() {
        _navToPartyDetail.value = null
    }

    private fun setHostName() {

        viewModelScope.launch {
            partiesResult.value?.forEach {
                it.hostName = FirebaseService.getUserById(it.hostId)?.name ?: ""
                Timber.d("Set host name${it.hostName}")
            }
            _parties.value = partiesResult.value
        }
    }

}