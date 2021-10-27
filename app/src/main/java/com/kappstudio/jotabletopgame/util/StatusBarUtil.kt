package com.kappstudio.jotabletopgame.util

import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance


fun statusBarUtil(activity: AppCompatActivity, isTransparent: Boolean) {
    if (isTransparent) {
        (activity).apply {
            // 進入沉浸式佈局
            ImmersionBar.with(this)
                .titleBar(findViewById<Toolbar>(R.id.toolbar))
                .init()
        }
    } else {
        (activity).apply {
            // 退出沉浸式佈局
            window.statusBarColor= appInstance.getColor(R.color.white_bg_f7f7f7)
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        }
    }
}
