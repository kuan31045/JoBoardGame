package com.kappstudio.joboardgame.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _navToMain = MutableLiveData<Boolean?>()
    val navToMain: LiveData<Boolean?>
        get() = _navToMain

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?>
        get() = _error

    fun addUser(user: User) {
        viewModelScope.launch {
            val result = FirebaseService.addUser(user)

            _error.value = when (result) {
                is Result.Success -> null
                is Result.Fail -> result.error
                is Result.Error -> result.exception.toString()
                else -> null
            }
            _navToMain.value = true
        }
    }

    fun getUserData(userId: String) {
        UserManager.user = FirebaseService.getLiveUser(userId)
    }
}