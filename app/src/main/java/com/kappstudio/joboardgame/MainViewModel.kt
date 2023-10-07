package com.kappstudio.joboardgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.util.PageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _page = MutableLiveData<PageType>()
    val page: LiveData<PageType> = _page

    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _isImmersion = MutableLiveData(false)
    val isImmersion: LiveData<Boolean> = _isImmersion

    fun getUserData(userId: String) {
        viewModelScope.launch(Dispatchers.IO + viewModelScope.coroutineContext) {
            userRepository.getUserByIdStream(userId).collect {
                UserManager.user.value = it
            }
        }
    }

    fun setBarStatus(status: PageType) {
        Timber.d("Status: $status")

        _page.value = status
        if (status != PageType.OTHER) {
            _title.value = status.title
        }

        _isImmersion.value =
            when (status) {
                PageType.GAME_DETAIL -> true
                PageType.PARTY_DETAIL -> true
                PageType.USER -> true
                else -> false
            }
    }
}