package com.kappstudio.joboardgame.party

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.partydetail.NavToPartyDetailInterface

class PartyViewModel(private val repository: JoRepository) : ViewModel(),
    NavToPartyDetailInterface {

    val parties: LiveData<List<Party>> = repository.getParties()

    private var _hosts = MutableLiveData<List<User>>()
    override val hosts: LiveData<List<User>>
        get() = _hosts

    fun getHosts() {
        val hostIdList = parties.value?.map { it.hostId }?.distinctBy { it }
        hostIdList?.let {
            _hosts = repository.getUsersByIdList(it)
        }
    }
}