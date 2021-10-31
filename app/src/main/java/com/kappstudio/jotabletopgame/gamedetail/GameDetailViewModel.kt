package com.kappstudio.jotabletopgame.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.source.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.source.JoRepository
import com.kappstudio.jotabletopgame.data.toGameMap
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

    val isFavorite = MutableLiveData(false)

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

    fun updateCollect() {
        Timber.d("updateCollect: ${isFavorite.value}")

        if (isFavorite.value == false) {
            //加入
            viewModelScope.launch {
                FirebaseService.addToFavorite(toGameMap(game.value ?: Game()))
                isFavorite.value = true
            }
        } else {
            //移除
            viewModelScope.launch {
                FirebaseService.removeFavorite(toGameMap(game.value ?: Game()))
                isFavorite.value = false
            }
        }


    }

    fun checkFavorite() {
        viewModelScope.launch {
            isFavorite.value = FirebaseService.checkFavorite(toGameMap(game.value ?: Game()))
        }
    }

}