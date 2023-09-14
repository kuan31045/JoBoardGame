package com.kappstudio.joboardgame.ui.favorite

import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.kappstudio.joboardgame.util.ToastUtil

class FavoriteViewModel(userId: String, private val repository: JoRepository) : ViewModel(),
    NavToGameDetailInterface {

    val user: LiveData<User> = repository.getUser(userId)

    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun removeFavorite(game: Game) {
        coroutineScope.launch {
            repository.removeFavorite(game.toGameMap()).collect {
                if (it is Result.Success) {
                    ToastUtil.show(appInstance.getString(R.string.favorite_out))
                }
            }
        }
    }
}