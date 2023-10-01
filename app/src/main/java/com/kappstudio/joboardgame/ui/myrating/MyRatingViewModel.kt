package com.kappstudio.joboardgame.ui.myrating

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface

class MyRatingViewModel(
    userId: String,
    gameRepository: GameRepository,
) : ViewModel(), NavToGameDetailInterface {

    val ratings: LiveData<List<Rating>> = gameRepository.getUserRatingsStream(userId).asLiveData()
}