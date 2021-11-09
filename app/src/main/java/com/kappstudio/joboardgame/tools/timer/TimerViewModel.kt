package com.kappstudio.joboardgame.tools.timer

import android.os.CountDownTimer
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {
    private lateinit var timer: CountDownTimer

    private val _time = MutableLiveData(30f)
    val time: LiveData<Float>
        get() = _time

    private val _progress = MutableLiveData(30f)
    val progress: LiveData<Float>
        get() = _progress

    val sec: LiveData<Int> = Transformations.map(progress) {
        it.toInt()
    }

    private val _isTiming = MutableLiveData(false)
    val isTiming: LiveData<Boolean>
        get() = _isTiming

    private val _isPause = MutableLiveData(false)
    val isPause: LiveData<Boolean>
        get() = _isPause

    fun setTime(t: Float) {

        stopTiming()
        _time.value = t
        _progress.value=_time.value

    }

    fun pause(){
        _isPause.value = true
    }



    fun startTiming() {

            _isTiming.value = true

            timer = object : CountDownTimer((time.value?.toLong() ?: 30000L) * 1000L, 10) {

                override fun onFinish() {
                    _progress.value = 0f
                    _isTiming.value = false
                    _isPause.value = false

                }

                override fun onTick(millisUntilFinished: Long) {
                    if (_isTiming.value == true) {
                        _progress.value = millisUntilFinished / 1000.toFloat()
                    }
                }
            }.start()

    }



    private fun stopTiming() {

        if (isTiming.value == true) {
            timer.cancel()
            _isTiming.value = false
        }
    }

}





