package com.kappstudio.joboardgame.util

import java.util.*

fun timeUtil(s: Long): String {
    val sec = (Calendar.getInstance().timeInMillis - s) / 1000
    return when {
        sec < 60 -> sec.toString() + "秒前"

        sec < 3600 -> (sec / 60).toString() + "分鐘前"

        sec < 86400 -> (sec / 3600).toString() + "小時前"

        sec < 604800 -> (sec / 86400).toString() + "天前"

        sec < 2592000 -> (sec / 604800).toString() + "週前"

        sec < 31536000 -> (sec / 2592000).toString() + "個月前"

        else -> (sec / 31536000).toString() + "年前"
    }
}