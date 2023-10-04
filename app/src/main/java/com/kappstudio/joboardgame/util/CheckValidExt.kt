package com.kappstudio.joboardgame.util

fun String?.checkValid(): Boolean {
    return when (this?.replace("\\s".toRegex(), "")) {
        null -> false
        "" -> false
        else -> true
    }
}