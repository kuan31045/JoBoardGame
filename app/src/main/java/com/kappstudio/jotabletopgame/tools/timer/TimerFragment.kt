package com.kappstudio.jotabletopgame.tools.timer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.databinding.FragmentDiceBinding
import com.kappstudio.jotabletopgame.databinding.FragmentTimerBinding


class TimerFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentTimerBinding.inflate(inflater)

        return binding.root
    }

}