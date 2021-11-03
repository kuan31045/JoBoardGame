package com.kappstudio.jotabletopgame.myrating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.Rating
import com.kappstudio.jotabletopgame.data.UserManager
import com.kappstudio.jotabletopgame.data.source.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.toGameMap
import com.kappstudio.jotabletopgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.jotabletopgame.rating.NavToRatingInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class MyRatingViewModel : ViewModel(),NavToGameDetailInterface,NavToRatingInterface {

    private var _games = FirebaseService.getLiveRatings(UserManager.user["id"]?:"")
    val games: LiveData<List<Rating>>
        get() = _games
    // nav
    private val _navToGameDetail = MutableLiveData<String?>()
    val navToGameDetail: LiveData<String?>
        get() = _navToGameDetail

    private val _navToRating = MutableLiveData<Rating?>()
    val navToRating: LiveData<Rating?>
        get() = _navToRating


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

    override fun navToRating(rating: Rating) {
        _navToRating.value = rating
    }

    override fun onNavToRating() {
        _navToRating.value = null
    }

}