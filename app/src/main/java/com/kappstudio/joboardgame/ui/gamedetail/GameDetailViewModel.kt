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

    val game: LiveData<Game> = gameRepository.getGameById(gameId).asLiveData()

    private val _navToRating = MutableLiveData<Rating?>()
    val navToRating: LiveData<Rating?>
        get() = _navToRating

    fun navToRating(rating: Rating) {
        _navToRating.value = rating
    }

    fun onNavToRating() {
        _navToRating.value = null
    }

    val isFavorite: LiveData<Boolean> = UserManager.user.map { user ->
        user.favoriteGames.map { it.id }.contains(gameId)
    }


    val myRating: LiveData<Rating?> = gameRepository.getMyRating(gameId).asLiveData()


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
             } else {
                userRepository.removeFavorite(game.value!!.toGameMap()).collect {
                    if (it is Result.Success) {
                        ToastUtil.show(appInstance.getString(R.string.favorite_out))
                    }
                }
             }
        }
    }


}