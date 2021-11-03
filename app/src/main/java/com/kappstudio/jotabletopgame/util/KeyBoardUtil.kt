package com.kappstudio.jotabletopgame.util

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun closeSoftKeyboard(view: View) {
    val imm = view.let { ContextCompat.getSystemService(it.context, InputMethodManager::class.java) }
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun closeKeyBoard(activity: Activity){
    activity.window?.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}