package com.kappstudio.jotabletopgame.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.sourc.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.User
import com.kappstudio.jotabletopgame.data.UserManager
import com.kappstudio.jotabletopgame.data.sourc.JoRepository
import com.kappstudio.jotabletopgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class ProfileViewModel(joRepository: JoRepository) : ViewModel(), NavToGameDetailInterface {
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


    // nav
    private val _navToGameDetail = MutableLiveData<String?>()
    val navToGameDetail: LiveData<String?>
        get() = _navToGameDetail

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

    override fun navToGameDetail(gameId: String) {
        if (gameId != "notFound") {
            _navToGameDetail.value = gameId
        } else {
            ToastUtil.show("資料庫內找不到這款遊戲，麻煩您自行去Google")
        }
    }


    override fun onNavToGameDetail() {
        _navToGameDetail.value = null
    }

}