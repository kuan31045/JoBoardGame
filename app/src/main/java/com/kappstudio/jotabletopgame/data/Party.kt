package com.kappstudio.jotabletopgame.data

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize

@Parcelize
data class Party(
    val id: String = "",
    val host: User? = null,
    var title: String = "",
    var partyTime: Timestamp = Timestamp.now(),
    var location: String = "",
    var note: String = "",
    var requirePlayerQty: Int = 0,
    var games: MutableList<String>? = null,
    var players: MutableList<String>? = null,
    var messages: MutableList<String>? = null,
): Parcelable