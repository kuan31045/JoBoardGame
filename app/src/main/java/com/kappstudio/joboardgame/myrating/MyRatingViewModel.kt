package com.kappstudio.joboardgame.myrating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.UserManager
import com.kappstudio.joboardgame.data.source.remote.FirebaseService
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.rating.NavToRatingInterface
import tech.gujin.toast.ToastUtil

class MyRatingViewModel : ViewModel(),NavToGameDetailInterface,NavToRatingInterface {

    private var _games = FirebaseService.getLiveRatings(UserManager.user["id"]?:"")
    val games: LiveData<List<Rating>>
        get() = _games



}