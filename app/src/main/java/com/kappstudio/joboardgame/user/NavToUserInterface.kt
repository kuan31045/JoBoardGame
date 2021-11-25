package com.kappstudio.joboardgame.user

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.login.UserManager

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