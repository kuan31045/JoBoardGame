package com.kappstudio.jotabletopgame.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import com.kappstudio.jotabletopgame.data.User
import com.kappstudio.jotabletopgame.data.UserManager
import com.kappstudio.jotabletopgame.user.UserInterface
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

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
            _user = FirebaseService.getLiveUserById(UserManager.user["id"] ?: "")

            _partyQty.value = FirebaseService.getUserParties(UserManager.user["id"] ?: "").size
            _hostQty.value = FirebaseService.getUserHosts(UserManager.user["id"] ?: "").size

        }
    }

}