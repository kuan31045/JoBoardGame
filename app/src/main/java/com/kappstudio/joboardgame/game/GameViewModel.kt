package com.kappstudio.joboardgame.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class GameViewModel : ViewModel(), NavToGameDetailInterface {

    private var _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games


    init {
        getGame()
    }

    private fun getGame() {
        viewModelScope.launch {
            _games= FirebaseService.getLiveGames()
        }
    }







}