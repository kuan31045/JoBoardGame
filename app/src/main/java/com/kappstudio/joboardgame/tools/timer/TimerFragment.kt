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

        binding.viewModel=viewModel
        binding.lifecycleOwner=viewLifecycleOwner

        binding.btn30s.setOnClickListener {
            viewModel.setTime(30f)
        }
        binding.btn60s.setOnClickListener {
            viewModel.setTime(60f)
        }
        binding.btn90s.setOnClickListener {
            viewModel.setTime(90f)
        }
        binding.btnStart.setOnClickListener {
            viewModel.startTiming()
        }
        binding.btnStop.setOnClickListener {
            viewModel.setTime(viewModel.time.value?.toFloat() ?: 30f)
        }
        binding.btnPause.setOnClickListener {
            viewModel.setTime(viewModel.sec.value?.toFloat() ?: 30f)
        }

        viewModel.time.observe(viewLifecycleOwner,{
            if (!viewModel.isPause.value!!){
                binding.cpbTimer.progressMax = it
            }
        })

        return binding.root
    }


}