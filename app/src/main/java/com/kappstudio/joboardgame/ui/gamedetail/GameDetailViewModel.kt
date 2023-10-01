package com.kappstudio.joboardgame.ui.gamedetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.util.ToastUtil
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GameDetailViewModel(
    private val gameId: String,
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    val game: LiveData<Game> = gameRepository.getGameByIdStream(gameId).asLiveData()

    private val _navToRating = MutableLiveData<Rating?>()
    val navToRating: LiveData<Rating?> = _navToRating

    val isFavorite: LiveData<Boolean> = UserManager.user.map { user ->
        user.favoriteGames.map { it.id }.contains(gameId)
    }

    val myRating: LiveData<Rating?> = gameRepository.getMyRatingStream(gameId).asLiveData()

    val avgRating: LiveData<Float?> = game.map { game ->
        if (game.ratingQty == 0L) {
            null
        } else {
            val avg = (game.totalRating.toFloat().div(game.ratingQty))
            ((avg * 10.0).roundToInt() / 10.0).toFloat()
        }
    }

    fun addViewedGame() {
        viewModelScope.launch {
            game.value?.let {
                gameRepository.upsertViewedGame(it)
            }
        }
    }

    fun toggleFavorite() {
        if (game.value == null) {
            return
        }

        viewModelScope.launch {
            when (isFavorite.value) {
                true -> {
                    if (userRepository.removeFavorite(game.value!!.toGameMap())) {
                        ToastUtil.showByRes(R.string.favorite_out)
                    }
                }

                false -> {
                    if (userRepository.insertFavorite(game.value!!.toGameMap())) {
                        ToastUtil.showByRes(R.string.favorite_in)
                    }
                }

                else -> {}
            }
        }
    }

    fun navToRating(rating: Rating?) {
        _navToRating.value = rating
            ?: Rating(
                id = "",
                gameId = gameId,
                game = game.value!!,
                score = 0
            )
    }

    fun onNavToRating() {
        _navToRating.value = null
    }
}