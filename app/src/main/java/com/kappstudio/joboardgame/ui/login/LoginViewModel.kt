package com.kappstudio.joboardgame.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _navToMain = MutableLiveData<Boolean?>()
    val navToMain: LiveData<Boolean?> = _navToMain

    fun addUser(user: User) {
        viewModelScope.launch {
            _navToMain.value = userRepository.addUser(user)
        }
    }
}