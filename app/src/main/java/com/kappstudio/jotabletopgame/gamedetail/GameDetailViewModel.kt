package com.kappstudio.jotabletopgame.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.source.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.source.JoRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class GameDetailViewModel(
    private val gameId: String,
    private val joRepository: JoRepository
) :
    ViewModel() {
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

    fun addViewedGame() {
        viewModelScope.launch {
            if (joRepository.getViewedGame(game.value?.id ?: "") == null) {
                joRepository.insertViewedGame(game.value ?: Game())
                Timber.d("database insert viewedGame: ${game.value?.name}")
            } else {
                joRepository.updateViewedGame(game.value ?: Game())
                Timber.d("database update viewedGame: ${game.value?.name}")
            }

        }
    }
}