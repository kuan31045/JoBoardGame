package com.kappstudio.joboardgame.ui.rating

import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.Rating

interface NavToRatingInterface {
    companion object {
        private val _navToRating = MutableLiveData<Rating?>()
    }

}