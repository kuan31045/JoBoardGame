package com.kappstudio.joboardgame.tools.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kappstudio.joboardgame.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    lateinit var binding: FragmentTimerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater)
        val viewModel: TimerViewModel by viewModels()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btn30s.setOnClickListener {
            viewModel.stopLottie()

            viewModel.setTime(30f)
        }
        binding.btn60s.setOnClickListener {
            viewModel.stopLottie()
            viewModel.setTime(60f)
        }
        binding.btn90s.setOnClickListener {
            viewModel.stopLottie()
            viewModel.setTime(90f)
        }
        binding.btnStart.setOnClickListener {
            viewModel.stopLottie()
            if (viewModel.isPause.value == false) {
                viewModel.totalTime.value?.let { it1 -> viewModel.setTime(it1) }
            }
            viewModel.startTiming()
        }
        binding.btnStop.setOnClickListener {
            viewModel.totalTime.value?.let { it1 -> viewModel.setTime(it1) }
            viewModel.setTime(viewModel.time.value?.toFloat() ?: 30f)
        }
        binding.btnPause.setOnClickListener {
            viewModel.pause()
        }

        viewModel.showLottie.observe(viewLifecycleOwner,{
            if (it){
                binding.lottieBug.visibility=View.VISIBLE

            }else{
                binding.lottieBug.visibility=View.GONE

            }
        })

        viewModel.time.observe(viewLifecycleOwner, {
            if (!viewModel.isPause.value!!) {
                binding.cpbTimer.progressMax = it
            }
        })

        return binding.root
    }


}