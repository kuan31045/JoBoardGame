package com.kappstudio.joboardgame.rating

import com.kappstudio.joboardgame.data.Rating

interface NavToRatingInterface {
    fun navToRating(rating: Rating)
    fun onNavToRating()
}