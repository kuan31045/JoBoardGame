package com.kappstudio.joboardgame.util

import android.widget.Toast
import androidx.annotation.StringRes
import com.kappstudio.joboardgame.appInstance

object ToastUtil {

    private const val duration = Toast.LENGTH_SHORT

    fun show(text: String) {
        val toast = Toast.makeText(appInstance, text, duration)
        toast.show()
    }

    fun showByRes(@StringRes stringRes: Int) {
        val toast = Toast.makeText(appInstance, appInstance.getString(stringRes), duration)
        toast.show()
    }
}