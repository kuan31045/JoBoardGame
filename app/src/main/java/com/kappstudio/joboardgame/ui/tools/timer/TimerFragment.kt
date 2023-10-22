package com.kappstudio.joboardgame.ui.tools.timer

import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.kappstudio.joboardgame.ui.theme.ComposeTheme

class TimerFragment : Fragment() {
    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val viewModel: TimerViewModel by viewModels()

        viewModel.setupInitTime(10L)
        viewModel.alertMessage.observe(viewLifecycleOwner) {
            if (it) {
                val vibrator = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(
                    (VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE))
                )
            }
        }

        return ComposeView(requireContext()).apply {
            setContent {
                ComposeTheme {
                    TimerScreen(viewModel)
                }
            }
        }
    }
}