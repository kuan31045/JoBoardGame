package com.kappstudio.joboardgame.tools.scoreboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentPolygraphBinding
 import com.kappstudio.joboardgame.databinding.FragmentScoreboardBinding
import com.kappstudio.joboardgame.tools.polygraph.PolygraphViewModel


class ScoreboardFragment : Fragment() {

    private lateinit var binding: FragmentScoreboardBinding
    val viewModel: PolygraphViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScoreboardBinding.inflate(inflater)


        return binding.root
    }

}