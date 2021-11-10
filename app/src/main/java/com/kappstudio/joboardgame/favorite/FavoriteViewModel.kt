package com.kappstudio.joboardgame.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.toGameMap
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class FavoriteViewModel : ViewModel(), NavToGameDetailInterface {

    private var _games = FirebaseService.getLiveFavorites()
    val games: LiveData<List<Game>>
        get() = _games


    fun removeFavorite(game: Game) {
        viewModelScope.launch {
            FirebaseService.removeFavorite(toGameMap(game))
        }
    }


}