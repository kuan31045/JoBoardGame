package com.kappstudio.joboardgame.ui.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
 import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.User

object UserManager {

    private const val USER_DATA = "user_data"
    private const val USER_TOKEN = "user_token"

    var user = MutableLiveData<User>()

    var userToken: String? = null
        get() = appInstance
            .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE)
            .getString(USER_TOKEN, null)
        set(value) {
            field = when (value) {
                null -> {
                    appInstance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .remove(USER_TOKEN)
                        .apply()
                    null
                }
                else -> {
                    appInstance
                        .getSharedPreferences(USER_DATA, Context.MODE_PRIVATE).edit()
                        .putString(USER_TOKEN, value)
                        .apply()
                    value
                }
            }
        }

    fun clear() {
        userToken = null
     }

 }