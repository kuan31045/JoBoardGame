package com.kappstudio.joboardgame.gamedetail

import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.Resource
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.data.toGameMap
import com.kappstudio.joboardgame.login.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.roundToInt
import kotlinx.coroutines.flow.collect
import tech.gujin.toast.ToastUtil

class GameDetailViewModel(
    private val gameId: String,
    private val repository: JoRepository
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

    val status = MutableLiveData(LoadApiStatus.LOADING)

    private var _myRating = MutableLiveData<Rating>()
    val myRating: LiveData<Rating>
        get() = _myRating

    val avgRating = MutableLiveData<Float>(0f)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        getGame()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private fun getGame() {
        viewModelScope.launch {
            _game = FirebaseService.getLiveGameById(gameId)
        }
    }

    fun addViewedGame() {
        viewModelScope.launch {
            if (repository.getViewedGame(game.value?.id ?: "") == null) {
                repository.insertViewedGame(game.value ?: Game())
                Timber.d("database insert viewedGame: ${game.value?.name}")
            } else {
                repository.updateViewedGame(game.value ?: Game())
                Timber.d("database update viewedGame: ${game.value?.name}")
            }

        }
    }

    fun updateCollect() {
        Timber.d("updateCollect: ${isFavorite.value}")
        if (isFavorite.value == false) {
            coroutineScope.launch {
                status.value = LoadApiStatus.LOADING
                repository.insertFavorite(toGameMap(game.value ?: Game())).collect {
                    if (it is Resource.Success) {
                        ToastUtil.show(appInstance.getString(R.string.favorite_in))
                    }
                }
                status.value = LoadApiStatus.DONE
                isFavorite.value = true
            }
        } else {
            coroutineScope.launch {
                repository.removeFavorite(toGameMap(game.value ?: Game())).collect {
                    if (it is Resource.Success) {
                        ToastUtil.show(appInstance.getString(R.string.favorite_out))
                    }
                }
                status.value = LoadApiStatus.DONE
                isFavorite.value = false
            }


        }


    }

    fun checkFavorite() {
        viewModelScope.launch {
            UserManager.user.value?.favoriteGames?.forEach {
                if (it.id == game.value?.id ?: "") {
                    isFavorite.value = true
                }
            }
        }
    }

    fun checkRating() {
        viewModelScope.launch {
            _myRating.value = game.value?.let { FirebaseService.getRating(it) }!!
        }
    }

    fun calAvgRating() {
        if (game.value?.ratingQty ?: 0 > 0) {
            val avg = (game.value?.totalRating?.toFloat()?.div(game.value?.ratingQty ?: 0))
            if (avg != null) {
                avgRating.value = ((avg * 10.0).roundToInt() / 10.0).toFloat()
            }
        }
    }
}