package com.kappstudio.joboardgame.game

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil

class GameViewModel : ViewModel(), NavToGameDetailInterface {

    private var _games = MutableLiveData<List<Game>>()
    val games: LiveData<List<Game>>
        get() = _games
    private  var isDismiss= false



    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch {
            if (!isDismiss){
                _games= FirebaseService.getLiveGames()

            }
        }
    }

    fun addFilter(type: String) {
       val list = games.value?.filter {
            it.type.contains(type)
        }
        _games.value = list!!
    }

    fun filter() {
        isDismiss = true
        Handler().postDelayed({
isDismiss=false
                              }, 2300)
    }


}