package com.kappstudio.jotabletopgame.game.all

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Game

class AllGameViewModel: ViewModel() {

    val games: LiveData<List<Game>?> = FirebaseService.getAllGames()


    init{
    //    FirebaseService.addMockGame()
    }
}