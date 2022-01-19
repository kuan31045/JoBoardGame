package com.kappstudio.joboardgame.ui.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface NavToUserInterface {
    companion object {
        private val _navToUser = MutableLiveData<String?>()
    }

    val navToUser: LiveData<String?>
        get() = _navToUser

    fun navToUser(userId: String) {
        _navToUser.value = userId
    }

    fun onNavToUser() {
        _navToUser.value = null
    }
}