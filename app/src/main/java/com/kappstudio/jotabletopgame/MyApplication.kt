package com.kappstudio.jotabletopgame

import android.app.Application
import tech.gujin.toast.ToastUtil
import timber.log.Timber

private lateinit var _appInstance: MyApplication
val appInstance: MyApplication
    get() = _appInstance

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ToastUtil.initialize(this)

        _appInstance = this

        //if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        //}
    }
}
