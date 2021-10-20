package com.kappstudio.jotabletopgame.game.featured

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Game

class FeaturedGameViewModel: ViewModel() {
    val games: LiveData<List<Game>?> = FirebaseService.getAllGames()



}