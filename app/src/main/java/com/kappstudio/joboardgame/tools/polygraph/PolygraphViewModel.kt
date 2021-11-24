package com.kappstudio.joboardgame.tools.polygraph

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PolygraphViewModel : ViewModel() {

    private val _isTesting = MutableLiveData(false)
    val isTesting: LiveData<Boolean>
        get() = _isTesting

    fun start() {
        _isTesting.value = true
    }

    fun stop() {
        _isTesting.value = false
    }
}