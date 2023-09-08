package com.kappstudio.joboardgame.ui.newparty.selectedgame

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.ui.login.UserManager

class SelectGameViewModel : ViewModel() {

    private var _games = MutableLiveData<List<Game>>(UserManager.user.value?.favoriteGames)
    val games: LiveData<List<Game>>
        get() = _games

    private var _selectedGames = MutableLiveData<MutableList<Game>>(mutableListOf())
    val selectedGames: LiveData<MutableList<Game>>
        get() = _selectedGames

    fun selectGame(game: Game) {
        if (selectedGames.value?.contains(game) == true) {
            _selectedGames.value?.remove(game)
            _selectedGames.value = _selectedGames.value
        } else {
            _selectedGames.value?.add(game)
            _selectedGames.value = _selectedGames.value
        }
    }

    fun clear() {
        _selectedGames.value?.clear()
        _selectedGames.value = _selectedGames.value
    }
}