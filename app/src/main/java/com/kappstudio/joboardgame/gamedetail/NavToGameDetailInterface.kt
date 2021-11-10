package com.kappstudio.joboardgame.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tech.gujin.toast.ToastUtil

interface NavToGameDetailInterface {
    companion object {
        private val _navToGameDetail = MutableLiveData<String?>()
    }

    val navToGameDetail: LiveData<String?>
        get() = _navToGameDetail

    fun navToGameDetail(gameId: String) {
        if (gameId != "notFound") {
            _navToGameDetail.value = gameId
        } else {
            ToastUtil.show("資料庫內找不到這款遊戲，麻煩您自行去Google")
        }
    }

    fun onNavToGameDetail() {
        _navToGameDetail.value = null
    }
}

