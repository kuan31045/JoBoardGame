package com.kappstudio.joboardgame.myrating

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface

class MyRatingViewModel(private val userId: String) : ViewModel(),NavToGameDetailInterface {

    private var _games = FirebaseService.getLiveRatings(userId)
    val games: LiveData<List<Rating>>
        get() = _games



}