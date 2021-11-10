package com.kappstudio.joboardgame.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.album.NavToAlbumInterface
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.UserManager
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch

class ProfileViewModel(joRepository: JoRepository) : ViewModel(), NavToGameDetailInterface,
    NavToAlbumInterface {
    val viewedGames: LiveData<List<Game>> = joRepository.getAllViewedGames()

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



    fun setStatus(status: String) {
        viewModelScope.launch {
            FirebaseService.setUserStatus(status)

        }

    }




}