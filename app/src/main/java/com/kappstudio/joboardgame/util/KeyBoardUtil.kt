package com.kappstudio.joboardgame.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat

fun closeSoftKeyboard(et:EditText) {
    val imm = et.let { ContextCompat.getSystemService(it.context, InputMethodManager::class.java) }
    imm?.hideSoftInputFromWindow(et.windowToken, 0)
}

fun closeKeyBoard(activity: Activity){
    activity.window?.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
}

fun openKeyBoard(et:EditText,activity: Activity){
    et.requestFocus()
    et.setOnFocusChangeListener { v, hasFocus ->
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }
}