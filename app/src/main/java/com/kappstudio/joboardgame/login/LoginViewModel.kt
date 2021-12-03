package com.kappstudio.joboardgame.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: JoRepository) : ViewModel() {
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
                is com.kappstudio.joboardgame.data.Resource.Success -> null
                is com.kappstudio.joboardgame.data.Resource.Fail -> result.error
                is com.kappstudio.joboardgame.data.Resource.Error -> result.exception.toString()
                else -> null
            }
            _navToMain.value = true
        }
    }
}