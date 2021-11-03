package com.kappstudio.jotabletopgame

import android.app.Application
import android.util.DisplayMetrics
import android.view.WindowManager
import com.kappstudio.jotabletopgame.data.source.DefaultJoRepository
import com.kappstudio.jotabletopgame.data.source.JoRepository
import com.kappstudio.jotabletopgame.data.source.local.JoLocalDataSource
import com.kappstudio.jotabletopgame.data.source.remote.JoRemoteDataSource
import tech.gujin.toast.ToastUtil
import timber.log.Timber
import kotlin.properties.Delegates

private lateinit var _appInstance: MyApplication
val appInstance: MyApplication
    get() = _appInstance

private var _screenHeight by Delegates.notNull<Int>()
val screenHeight: Int
    get() = _screenHeight
class MyApplication : Application() {

    private var joRepository: JoRepository? = null

    fun provideJoRepository(): JoRepository {
        synchronized(this) {
            return joRepository ?: DefaultJoRepository(
                JoRemoteDataSource,
                JoLocalDataSource(this)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        ToastUtil.initialize(this)
        // 獲取螢幕高度
        val displayMetrics = DisplayMetrics()
        val windowsManager = this.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        _screenHeight = displayMetrics.heightPixels
        _appInstance = this

        //if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        //}
    }
}
