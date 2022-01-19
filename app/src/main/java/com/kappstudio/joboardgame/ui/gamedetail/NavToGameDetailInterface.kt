package com.kappstudio.joboardgame.ui.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.Game

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

