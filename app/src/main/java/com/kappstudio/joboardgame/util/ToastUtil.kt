package com.kappstudio.joboardgame.util

import android.widget.Toast
import com.kappstudio.joboardgame.appInstance

object ToastUtil {
    fun show(text: String) {
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(appInstance, text, duration)
        toast.show()
    }
}