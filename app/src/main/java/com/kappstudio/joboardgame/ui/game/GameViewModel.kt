package com.kappstudio.joboardgame.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface

class GameViewModel(
    gameRepository: GameRepository,
) : ViewModel(), NavToGameDetailInterface {

    val games: LiveData<List<Game>> = gameRepository.getGamesStream().asLiveData()
}