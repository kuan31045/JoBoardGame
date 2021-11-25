package com.kappstudio.joboardgame.tools.timer


import androidx.lifecycle.*
import kotlinx.coroutines.*

class TimerViewModel  : ViewModel() {

    private var job: Job? = null

    private val startTime = MutableLiveData(0L)
    val timeState = MutableLiveData(0L)
    val alertState = MutableLiveData(false)
    val alertMessage = MutableLiveData(false)

    fun setupInitTime(time: Long) {
        startTime.value = timeState.value

        viewModelScope.launch(Dispatchers.IO) {
            timeState.postValue(time)
            alertState.postValue(false)
            alertMessage.postValue(false)
        }
    }

    // Time in second
    fun startTimer() {
        stop()
        job = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                if (timeState.value!! <= 0L) {
                    job?.cancel()
                    return@launch
                }
                alertMessage.postValue(timeState.value!! - 1 == 0L)
                alertState.postValue(timeState.value!! - 1 <= 5)
                timeState.postValue((timeState.value!! - 1).coerceAtLeast(0L))
                delay(1_000)
            }
        }
    }

    fun stop() {
        job?.cancel()
        startTime.value?.let { setupInitTime(it) }
    }

    fun addTime(time: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            timeState.postValue(timeState.value!! + time)
        }
    }

    fun minusTime(time: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            if (timeState.value!! > 10) {
                timeState.postValue(timeState.value!! - time)
            }
        }
    }
}
