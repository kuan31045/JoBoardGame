package com.kappstudio.joboardgame.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kappstudio.joboardgame.data.Rating

interface NavToRatingInterface {
    companion object {
        private val _navToRating = MutableLiveData<Rating?>()
    }

}