package com.kappstudio.joboardgame.ui.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.NewRating
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.util.ConnectivityUtil
import kotlinx.coroutines.launch

class RatingViewModel(
    private val rating: Rating,
    private val gameRepository: GameRepository,
) : ViewModel() {

    private var _myRating = MutableLiveData(rating)
    val myRating: LiveData<Rating> = _myRating

    private val _toastMsgRes = MutableLiveData<Int>()
    val toastMsgRes: LiveData<Int> = _toastMsgRes

    val hasRating = MutableLiveData(rating.id.isNotEmpty())

    var score = MutableLiveData(rating.score)

    private val _dismiss = MutableLiveData<Boolean>()
    val dismiss: LiveData<Boolean> = _dismiss

    fun sendRating() {
        if (ConnectivityUtil.isNotConnected()) {
            _toastMsgRes.value = R.string.check_internet
            return
        }

        val newRating = NewRating(
            id = rating.id,
            gameId = rating.gameId,
            game = rating.game.toGameMap(),
            score = score.value ?: 0
        )

        viewModelScope.launch {
            if (gameRepository.upsertMyRating(newRating)) {
                _toastMsgRes.value = R.string.rating_ok
                _dismiss.value = true
            }
            gameRepository.updateGameRating(rating, (score.value ?: 0) - rating.score)
        }
    }

    fun removeRating() {
        if (ConnectivityUtil.isNotConnected()) {
            _toastMsgRes.value = R.string.check_internet
            return
        }

        viewModelScope.launch {
            if (gameRepository.removeMyRating(rating)
            ) {
                _toastMsgRes.value = R.string.remove_ok
                _dismiss.value = true
            }
        }
    }
}