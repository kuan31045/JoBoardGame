package com.kappstudio.joboardgame.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.repository.UserRepository
import kotlinx.coroutines.launch
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.util.ToastUtil

class LoginViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _navToMain = MutableLiveData<Boolean?>()
    val navToMain: LiveData<Boolean?>
        get() = _navToMain

    fun addUser(user: User) {
        viewModelScope.launch {
            when (userRepository.login(user)) {
                is Result.Success -> _navToMain.value = true
                else -> ToastUtil.show(appInstance.getString(R.string.login_fail))
            }
        }
    }
}