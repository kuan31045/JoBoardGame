package com.kappstudio.jotabletopgame.game.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Game
import kotlinx.coroutines.launch

class AllGameViewModel : ViewModel() {

    private var _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games


    init {
        getGame()
    }

    private fun getGame() {
        viewModelScope.launch {
            _games.value = FirebaseService.getGames()
        }
    }


}