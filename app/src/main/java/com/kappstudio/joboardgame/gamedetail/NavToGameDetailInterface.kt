package com.kappstudio.joboardgame.gamedetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Game
import tech.gujin.toast.ToastUtil

interface NavToGameDetailInterface {
    companion object {
        private val _navToGameDetail = MutableLiveData<Game?>()
    }

    val navToGameDetail: LiveData<Game?>
        get() = _navToGameDetail

    fun navToGameDetail(game: Game) {
        _navToGameDetail.value = game


    }

    fun onNavToGameDetail() {
        _navToGameDetail.value = null
    }
}

