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

    private var isDismiss = false


    init {
        getGames()
    }

    private fun getGames() {

    }
//
//    fun addFilter(type: String) {
//        val list = games.value?.filter {
//            it.type.contains(type)
//        }
//        _games.value = list!!
//    }
//
//    fun filter() {
//        isDismiss = true
//        Handler().postDelayed({
//            isDismiss = false
//        }, 2300)
//    }


}