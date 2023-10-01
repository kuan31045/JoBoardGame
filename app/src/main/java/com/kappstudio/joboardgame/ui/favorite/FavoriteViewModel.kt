package com.kappstudio.joboardgame.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch

class FavoriteViewModel(
    val userId: String,
    private val userRepository: UserRepository,
) : ViewModel(), NavToGameDetailInterface {

    val user: LiveData<User> = userRepository.getUserStream(userId).asLiveData()

    fun removeFavorite(game: Game) {
        viewModelScope.launch {
            userRepository.removeFavorite(game.toGameMap())
        }
    }
}