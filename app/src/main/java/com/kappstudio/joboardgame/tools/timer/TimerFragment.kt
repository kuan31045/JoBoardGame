package com.kappstudio.joboardgame.tools.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
 import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import com.kappstudio.joboardgame.compose.theme.ComposeTimerTheme
import com.kappstudio.joboardgame.tools.drawlots.DrawLotsViewModel


class TimerFragment : Fragment() {

    lateinit var viewModel: TimerViewModel 

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this)[TimerViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

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