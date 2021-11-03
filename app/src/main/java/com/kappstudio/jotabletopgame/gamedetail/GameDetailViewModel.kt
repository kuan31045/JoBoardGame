package com.kappstudio.jotabletopgame.gamedetail

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.data.source.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.Rating
import com.kappstudio.jotabletopgame.data.source.JoRepository
import com.kappstudio.jotabletopgame.data.toGameMap
import com.kappstudio.jotabletopgame.rating.NavToRatingInterface
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

    // nav
    private val _navToRating = MutableLiveData<Rating?>()
    val navToRating: LiveData<Rating?>
        get() = _navToRating

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
            isFavorite.value = FirebaseService.checkFavorite(toGameMap(game.value ?: Game()))
        }
    }

    fun checkRating() {
        viewModelScope.launch {
            _myRating.value = game.value?.let { FirebaseService.getRating(it) }!!
        }
    }

    override fun navToRating(rating: Rating) {
        _navToRating.value = rating
    }

    override fun onNavToRating() {
        _navToRating.value = null
    }

    fun calAvgRating() {
        val avg =  (game.value?.totalRating?.toFloat()?.div(game.value?.ratingQty?:0))
        if (avg != null) {
            avgRating.value = ((avg * 10.0).roundToInt() / 10.0).toFloat()
        }


    }



}