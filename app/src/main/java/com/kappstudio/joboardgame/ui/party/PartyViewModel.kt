package com.kappstudio.joboardgame.ui.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface

class PartyViewModel(
    partyRepository: PartyRepository,
    private val userRepository: UserRepository,
) : ViewModel(), NavToPartyDetailInterface {

    val parties: LiveData<List<Party>> = partyRepository.getParties()

    private var _hosts = MutableLiveData<List<User>>()
    override val hosts: LiveData<List<User>>
        get() = _hosts

    val status = MutableLiveData(LoadApiStatus.LOADING)

    fun getHosts() {
        val hostIdList = parties.value?.map { it.hostId }?.distinctBy { it }
        hostIdList?.let {
            _hosts = userRepository.getUsersByIdList(it)
        }
    }
}