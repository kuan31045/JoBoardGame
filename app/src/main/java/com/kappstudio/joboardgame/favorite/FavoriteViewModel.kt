package com.kappstudio.joboardgame.favorite

import android.view.animation.Transformation
import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.toGameMap
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.login.UserManager
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class FavoriteViewModel : ViewModel(), NavToGameDetailInterface {

    val games: LiveData<List<Game>> = Transformations.map(UserManager.user){
        it.favoriteGames
    }


    fun removeFavorite(game: Game) {
        viewModelScope.launch {
            FirebaseService.removeFavorite(toGameMap(game))
        }
    }


}