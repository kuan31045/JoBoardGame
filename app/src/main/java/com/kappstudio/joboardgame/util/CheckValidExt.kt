package com.kappstudio.joboardgame.util

fun String?.checkValid(): Boolean {
    return when (this?.trim()) {
        null -> false
        "" -> false
        else -> true
    }
}