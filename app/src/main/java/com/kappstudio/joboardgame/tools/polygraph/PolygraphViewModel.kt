package com.kappstudio.joboardgame.tools.polygraph

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.allUsers
import com.kappstudio.joboardgame.bindImage
import timber.log.Timber

class PolygraphViewModel : ViewModel() {

    private var initLieValue: Int? = null

    private val _lieValue = MutableLiveData(0)
    val lieValue: LiveData<Int>
        get() = _lieValue

    private val _isTesting = MutableLiveData(false)
    val isTesting: LiveData<Boolean>
        get() = _isTesting

    private val _navTolie = MutableLiveData<Boolean?>()
    val navTolie: LiveData<Boolean?>
        get() = _navTolie

    fun start() {
        initLieValue = null
        _isTesting.value = true
    }

    fun stop() {
        _isTesting.value = false
    }

    fun calEventValue(values: FloatArray) {

        val calValue: (Float) -> Int = { n: Float -> (Math.abs(n) * 100).toInt() }

        if (isTesting.value == true) {

            Timber.d("   ${values[2]}")

            if (initLieValue == null) {
                initLieValue = ((calValue(values[0]) + calValue(values[1]) + calValue(values[2])) * 0.8 ).toInt()
            }

            val newLieValue = ((calValue(values[0]) + calValue(values[1]) + calValue(values[2])) * 0.8 ).toInt()
            _lieValue.value = Math.abs(newLieValue - initLieValue!!)
            if (lieValue.value!! > 100) {
                stop()
                _navTolie.value = true
            }
        }

    }

    fun onNavToLie() {
        _navTolie.value = null
    }
}