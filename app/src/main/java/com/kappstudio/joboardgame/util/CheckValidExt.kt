package com.kappstudio.joboardgame.util

fun String?.checkValid() = this?.replace("\\s".toRegex(), "")?.isEmpty() == true

fun String?.checkEmpty() = this?.replace("\\s".toRegex(), "") != ""