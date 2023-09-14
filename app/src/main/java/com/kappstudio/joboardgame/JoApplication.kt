package com.kappstudio.joboardgame

import android.app.Application
import android.util.DisplayMetrics
import android.view.WindowManager
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.data.source.JoRepositoryImpl
import com.kappstudio.joboardgame.data.source.room.JoLocalDataSource
import com.kappstudio.joboardgame.data.source.remote.JoRemoteDataSource
import com.kappstudio.joboardgame.di.appModule
import com.kappstudio.joboardgame.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import kotlin.properties.Delegates

private lateinit var _appInstance: JoApplication
val appInstance: JoApplication
    get() = _appInstance

private var _screenHeight by Delegates.notNull<Int>()
val screenHeight: Int
    get() = _screenHeight

class JoApplication : Application() {

    private var joRepository: JoRepository? = null

    fun provideJoRepository(): JoRepository {
        synchronized(this) {
            return joRepository ?: JoRepositoryImpl(
                JoRemoteDataSource,
                JoLocalDataSource(this)
            )
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@JoApplication)
            modules(appModule, repositoryModule)
        }

        val displayMetrics = DisplayMetrics()
        val windowsManager = this.getSystemService(WINDOW_SERVICE) as WindowManager
        windowsManager.defaultDisplay.getMetrics(displayMetrics)
        _screenHeight = displayMetrics.heightPixels
        _appInstance = this

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}