package com.kappstudio.jotabletopgame.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.User
import com.kappstudio.jotabletopgame.data.source.remote.FirebaseService
import kotlinx.coroutines.launch

class UserViewModel(private val userId: String):ViewModel() {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user
    private var _partyQty = MutableLiveData(0)
    val partyQty: LiveData<Int>
        get() = _partyQty

    private var _hostQty = MutableLiveData(0)
    val hostQty: LiveData<Int>
        get() = _hostQty



    init {
        getUserInfo()
    }
    private fun getUserInfo() {
        viewModelScope.launch {
            _user = FirebaseService.getLiveUserById(userId)

            _partyQty.value = FirebaseService.getUserParties(userId).size
            _hostQty.value = FirebaseService.getUserHosts(userId).size

        }
    }
}