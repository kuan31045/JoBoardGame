package com.kappstudio.joboardgame.tools.polygraph

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.airbnb.lottie.animation.content.Content
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.DialogLieBinding

class LieDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val binding = DialogLieBinding.inflate(inflater, container, false)

        val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(
            (VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        )
        return binding.root
    }
}