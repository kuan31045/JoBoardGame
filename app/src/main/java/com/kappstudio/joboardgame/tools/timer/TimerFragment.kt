package com.kappstudio.joboardgame.tools.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import com.kappstudio.joboardgame.compose.theme.ComposeTimerTheme
 

class TimerFragment : Fragment() {

    private val viewModel: TimerViewModel by viewModels()

    @ExperimentalAnimationApi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel.setupInitTime(10L)
        return ComposeView(requireContext()).apply {
            setContent {
                ComposeTimerTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colors.background) {
                        TimerScreen(viewModel)
                    }
                }
            }
        }
    }


}