package com.kappstudio.joboardgame.util

import android.app.Activity
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat


fun closeSoftKeyboard(et: EditText) {
    val imm = et.let { ContextCompat.getSystemService(it.context, InputMethodManager::class.java) }
    imm?.hideSoftInputFromWindow(et.windowToken, 0)
}


fun closeKeyBoard(activity: Activity) {
    activity.window?.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
    )
}

fun openKeyBoard(et: EditText) {
    et.requestFocus()
    et.setOnFocusChangeListener { v, hasFocus ->
        et.let { ContextCompat.getSystemService(it.context, InputMethodManager::class.java) }
            ?.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }
}