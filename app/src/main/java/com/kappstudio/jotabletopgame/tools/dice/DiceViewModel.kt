package com.kappstudio.jotabletopgame.tools.dice

import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.jotabletopgame.R
import timber.log.Timber
import java.util.*

class DiceViewModel : ViewModel() {
    val qty = MutableLiveData(1)
    val isRolling = MutableLiveData(false)
    val total = MutableLiveData(0)
    val diceImages =
        listOf(
            R.drawable.image_dice_1,
            R.drawable.image_dice_2,
            R.drawable.image_dice_3,
            R.drawable.image_dice_4,
            R.drawable.image_dice_5,
            R.drawable.image_dice_6
        )

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
        return diceImages[num - 1]
    }

    fun roll() {
        Timber.d("roll()")
        total.value = 0
        isRolling.value = true
    }
}