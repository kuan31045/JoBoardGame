package com.kappstudio.joboardgame.game

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.allGames
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface

class GameViewModel : ViewModel(), NavToGameDetailInterface {

     val games: LiveData<List<Game>>
        get() = allGames



    init {
        getGames()
    }

    private fun getGames() {

    }


}