package com.kappstudio.joboardgame.myrating

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.rating.NavToRatingInterface

class MyRatingViewModel : ViewModel(),NavToGameDetailInterface,NavToRatingInterface {

    private var _games = FirebaseService.getLiveRatings(UserManager.user.value?.id?:"")
    val games: LiveData<List<Rating>>
        get() = _games



}