package com.kappstudio.jotabletopgame.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.source.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.toGameMap
import com.kappstudio.jotabletopgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class FavoriteViewModel:ViewModel(), NavToGameDetailInterface {

    private var _games = FirebaseService.getLiveFavorites()
    val games: LiveData<List<Game>>
        get() = _games

    // nav
    private val _navToGameDetail = MutableLiveData<String?>()
    val navToGameDetail: LiveData<String?>
        get() = _navToGameDetail

    override fun navToGameDetail(gameId: String) {
        if (gameId != "notFound") {
            _navToGameDetail.value = gameId
        } else {
            ToastUtil.show("資料庫內找不到這款遊戲，麻煩您自行去Google")
        }
    }


    override fun onNavToGameDetail() {
        _navToGameDetail.value = null
    }

    fun removeFavorite(game: Game){
        viewModelScope.launch {
            FirebaseService.removeFavorite(toGameMap(game))
        }
    }



}