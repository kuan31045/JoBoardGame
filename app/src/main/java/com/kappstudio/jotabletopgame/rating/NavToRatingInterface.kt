package com.kappstudio.jotabletopgame.rating

import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.data.Rating

interface NavToRatingInterface {
    fun navToRating(rating: Rating)
    fun onNavToRating()
}