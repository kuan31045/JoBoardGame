package com.kappstudio.joboardgame.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    var address: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
) : Parcelable