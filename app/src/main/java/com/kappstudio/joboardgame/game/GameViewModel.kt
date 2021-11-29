package com.kappstudio.joboardgame.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface

class GameViewModel(repository: JoRepository) : ViewModel(),
    NavToGameDetailInterface {

    val games: LiveData<List<Game>> = repository.getGames()

}