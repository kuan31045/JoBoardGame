package com.kappstudio.joboardgame.util

import android.view.View
import android.view.WindowManager
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.gyf.barlibrary.ImmersionBar
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance


fun statusBarUtil(activity: AppCompatActivity, isTransparent: Boolean) {
    if (isTransparent) {
        (activity).apply {

            // 進入沉浸式佈局
            ImmersionBar.with(this)
                .titleBar(findViewById<Toolbar>(R.id.toolbar))
                .init()
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        }
    } else {
        (activity).apply {
            // 退出沉浸式佈局
            window.statusBarColor= appInstance.getColor(R.color.white )
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
            activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }
}
