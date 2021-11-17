package com.kappstudio.joboardgame.gamedetail

import androidx.lifecycle.*
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.toGameMap
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.rating.NavToRatingInterface
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt

class GameDetailViewModel(
    private val gameId: String,
    private val joRepository: JoRepository
) :
    ViewModel(), NavToRatingInterface {
    private var _game = MutableLiveData<Game>()
    val game: LiveData<Game>
        get() = _game

    val isFavorite = MutableLiveData(false)

    private var _myRating = MutableLiveData<Rating>()
    val myRating: LiveData<Rating>
        get() = _myRating


    val avgRating = MutableLiveData<Float>(0f)


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
            isFavorite.value = UserManager.user.value?.favoriteGames?.contains(game.value)
         }
    }

    fun checkRating() {
        viewModelScope.launch {
            _myRating.value = game.value?.let { FirebaseService.getRating(it) }!!
        }
    }


    fun calAvgRating() {
        val avg =  (game.value?.totalRating?.toFloat()?.div(game.value?.ratingQty?:0))
        if (avg != null) {
            avgRating.value = ((avg * 10.0).roundToInt() / 10.0).toFloat()
        }


    }



}