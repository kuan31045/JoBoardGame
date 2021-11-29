package com.kappstudio.joboardgame.myrating

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.gamedetail.NavToGameDetailInterface

class MyRatingViewModel(userId: String, repository: JoRepository) : ViewModel(),
    NavToGameDetailInterface {

    val ratings: LiveData<List<Rating>> = repository.getUserRatings(userId)
}