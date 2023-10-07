package com.kappstudio.joboardgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val address: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
) : Parcelable