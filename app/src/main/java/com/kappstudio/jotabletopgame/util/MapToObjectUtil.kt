package com.kappstudio.jotabletopgame.util

import com.kappstudio.jotabletopgame.data.User

fun mapsToUsers(maps : MutableList<HashMap<String, String>>): MutableList<User> {
    val users = mutableListOf<User>()
    maps.forEach {
        users.add( User(
            id = it["id"] ?: "",
            name = it["name"] ?: "",
            image = it["image"] ?: ""
        )
        )
    }
    return users
}