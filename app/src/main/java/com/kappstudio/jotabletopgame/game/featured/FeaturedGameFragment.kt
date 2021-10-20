package com.kappstudio.jotabletopgame.game.featured

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.data.Game
import com.kappstudio.jotabletopgame.databinding.FragmentGameAllBinding
import com.kappstudio.jotabletopgame.databinding.FragmentGameFeaturedBinding
import com.kappstudio.jotabletopgame.game.GameAdapter
import com.kappstudio.jotabletopgame.game.all.AllGameViewModel
import java.util.*

class FeaturedGameFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameFeaturedBinding.inflate(inflater)
        val viewModel: AllGameViewModel by viewModels()
        viewModel.games.observe(viewLifecycleOwner,{
            binding.rvHots.adapter = GameAdapter(1).apply {
                submitList(it)
            }
            binding.rvRecommend.adapter = GameAdapter(1).apply {

                submitList(it?.shuffled())
            }
            binding.rvTopRating.adapter = GameAdapter(1).apply {
                submitList(it)
            }


        })



        return binding.root
    }

}