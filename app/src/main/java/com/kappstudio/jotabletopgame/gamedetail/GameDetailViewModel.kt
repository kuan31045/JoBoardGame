package com.kappstudio.jotabletopgame.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Game
import kotlinx.coroutines.launch

class GameDetailViewModel(private val gameId: String):ViewModel() {
    private var _game = MutableLiveData<Game>()
    val game: LiveData<Game>
        get() = _game
    init {
        getGame()
    }

    private fun getGame() {
        viewModelScope.launch {
            _game = FirebaseService.getLiveGameById(gameId)
        }
    }
}