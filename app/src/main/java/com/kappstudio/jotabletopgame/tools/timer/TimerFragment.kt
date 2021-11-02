package com.kappstudio.jotabletopgame.tools.timer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.jotabletopgame.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    lateinit var binding: FragmentTimerBinding
    private lateinit var timer: CountDownTimer
    var isTiming: Boolean = false
    var time = 30f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTimerBinding.inflate(inflater)
        binding.btn30s.setOnClickListener {
            time = 30f
            setTime()
            stopTimeing()
        }


        binding.btn60s.setOnClickListener {
            time = 60f
            setTime()

            stopTimeing()

        }
        binding.btn90s.setOnClickListener {
            time = 90f
            setTime()

            stopTimeing()
        }
        binding.btnStart.setOnClickListener {
            timing()
        }
        return binding.root
    }

    private fun timing() {
        stopTimeing()
        isTiming=true
        setTime()

        timer = object : CountDownTimer(time.toLong()*1000L, 10) {

            override fun onFinish() {
                binding.tvTime.text = "0"
                binding.cpbTimer.progress = 0f
                isTiming=false

            }

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTime.text = "${millisUntilFinished / 1000}"
                binding.cpbTimer.progress = millisUntilFinished / 1000.toFloat()
            }
        }.start()
    }


    private fun stopTimeing() {
        if(isTiming){
            timer.cancel()
            isTiming=false


        }
    }

    private fun setTime(){
        binding.cpbTimer.progress = time
        binding.cpbTimer.progressMax = time

        binding.tvTime.text =time.toInt().toString()
    }


}