package com.kappstudio.joboardgame.tools.timer

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.joboardgame.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {

    lateinit var binding: FragmentTimerBinding
    private lateinit var timer: CountDownTimer
    var isTiming: Boolean = false
    var time = 30f
    var isPause = false

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
            isPause = false
            timing()

        }
        binding.btnStop.setOnClickListener {
             setTime()
            stopTimeing()
        }
        binding.btnPause.setOnClickListener {
            binding.btnStart.visibility = View.VISIBLE

            isPause = true
        }
        return binding.root
    }

    private fun timing() {
        stopTimeing()
        isTiming = true
        setTime()

        binding.btnStart.visibility = View.INVISIBLE
        binding.btnStop.visibility = View.VISIBLE
        binding.btnPause.visibility = View.VISIBLE

        timer = object : CountDownTimer(time.toLong() * 1000L, 10) {

            override fun onFinish() {
                binding.tvTime.text = "0"
                binding.cpbTimer.progress = 0f
                isTiming = false

            }

            override fun onTick(millisUntilFinished: Long) {
                if (!isPause){
                    binding.tvTime.text = "${millisUntilFinished / 1000}"
                    binding.cpbTimer.progress = millisUntilFinished / 1000.toFloat()
                }


            }
        }.start()
    }


    private fun stopTimeing() {
        if (isTiming) {
            timer.cancel()
            isTiming = false

            binding.btnStart.visibility = View.VISIBLE
            binding.btnStop.visibility = View.GONE
            binding.btnPause.visibility = View.GONE
        }
    }

    private fun setTime() {
        binding.cpbTimer.progress = time
        binding.cpbTimer.progressMax = time

        binding.tvTime.text = time.toInt().toString()
    }


}