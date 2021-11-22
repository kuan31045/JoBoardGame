package com.kappstudio.joboardgame.tools.dice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber
import java.util.*

class DiceViewModel : ViewModel() {
    val qty = MutableLiveData(1)
    val isRolling = MutableLiveData(false)
    val total = MutableLiveData(0)


    fun plusQty() {
        qty.value = qty.value?.plus(1)
        Timber.d("plusQty: ${qty.value}")
    }

    fun minusQty() {
        qty.value = qty.value?.minus(1)
        Timber.d("minusQty: ${qty.value}")
    }

    fun getRollResult(): Int {
        val num = Random().nextInt(6) + 1
        total.value = total.value?.plus(num)
        Timber.d("roll result: $num")
        isRolling.value = false

        return when (num) {
            1 -> 25
            2 -> 30
            3 -> 35
            4 -> 40
            5 -> 45
            else -> 50
        }

    }

    fun roll() {
        Timber.d("roll()")
        total.value = 0
        isRolling.value = true
    }
}