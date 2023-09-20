package com.kappstudio.joboardgame.util

import android.content.Context
import com.kappstudio.joboardgame.appInstance
import android.net.ConnectivityManager

object ConnectivityUtil {
   private val connectivityManager =
        appInstance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun isNotConnected() = connectivityManager.activeNetwork == null
}