package com.kappstudio.joboardgame.ui.gamedetail

import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import com.kappstudio.joboardgame.util.ToastUtil

class GameDetailViewModel(
    private val gameId: String,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private var _game = MutableLiveData<Game>()
    val game: LiveData<Game>
        get() = _game

    private val _navToRating = MutableLiveData<Rating?>()
    val navToRating: LiveData<Rating?>
        get() = _navToRating

    fun navToRating(rating: Rating) {
        _navToRating.value = rating
    }

    fun onNavToRating() {
        _navToRating.value = null
    }

    val isFavorite = MutableLiveData(false)


    private var _myRating = MutableLiveData<Rating>()
    val myRating: LiveData<Rating>
        get() = _myRating

    val avgRating = MutableLiveData(0f)

    init {
        getGame()
    }

    private fun getGame() {
        viewModelScope.launch {
            _game = gameRepository.getGameById(gameId)
        }
    }

    fun addViewedGame() {
        viewModelScope.launch {
            game.value?.let {
                gameRepository.upsertViewedGame(it)
            }
        }
    }

    fun updateCollect() {
        if (game.value == null) {
            return
        }

        viewModelScope.launch {
            if (isFavorite.value == false) {
                userRepository.insertFavorite(game.value!!.toGameMap()).collect {
                    if (it is Result.Success) {
                        ToastUtil.show(appInstance.getString(R.string.favorite_in))
                    }
                }
                isFavorite.value = true
            } else {
                userRepository.removeFavorite(game.value!!.toGameMap()).collect {
                    if (it is Result.Success) {
                        ToastUtil.show(appInstance.getString(R.string.favorite_out))
                    }
                }
                isFavorite.value = false
            }
        }
    }

    fun checkFavorite() {
        viewModelScope.launch {
            UserManager.user.value?.favoriteGames?.forEach {
                if (it.id == (game.value?.id ?: "")) {
                    isFavorite.value = true
                }
            }
        }
    }

    fun checkRating() {
        viewModelScope.launch {
            _myRating.value = game.value?.let {
                gameRepository.getRating(it) ?: Rating(
                    gameId = it.id,
                    game = it,
                    userId = UserManager.user.value?.id ?: ""
                )
            }
        }
    }

    fun calAvgRating() {
        if ((game.value?.ratingQty ?: 0) <= 0) {
            return
        }

        val avg = (game.value?.totalRating?.toFloat()?.div(game.value?.ratingQty ?: 0))
        if (avg != null) {
            avgRating.value = ((avg * 10.0).roundToInt() / 10.0).toFloat()
        }
    }
}