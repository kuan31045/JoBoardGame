package com.kappstudio.jotabletopgame

import android.app.Application
import com.kappstudio.jotabletopgame.data.sourc.DefaultJoRepository
import com.kappstudio.jotabletopgame.data.sourc.JoRepository
import com.kappstudio.jotabletopgame.data.sourc.local.JoLocalDataSource
import com.kappstudio.jotabletopgame.data.sourc.remote.JoRemoteDataSource
import tech.gujin.toast.ToastUtil
import timber.log.Timber

private lateinit var _appInstance: MyApplication
val appInstance: MyApplication
    get() = _appInstance

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

        _appInstance = this

        //if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        //}
    }
}
