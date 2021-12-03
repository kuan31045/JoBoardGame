package com.kappstudio.joboardgame.favorite

import androidx.lifecycle.*
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Resource
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.toGameMap
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

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
            repository.removeFavorite(toGameMap(game)).collect {
                if (it is Resource.Success) {
                    ToastUtil.show(appInstance.getString(R.string.favorite_out))
                }
            }
        }
    }
}