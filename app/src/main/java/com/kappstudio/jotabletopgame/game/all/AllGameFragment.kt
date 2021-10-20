package com.kappstudio.jotabletopgame.game.all

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.kappstudio.jotabletopgame.databinding.FragmentGameAllBinding
import com.kappstudio.jotabletopgame.databinding.FragmentGameFeaturedBinding
import com.kappstudio.jotabletopgame.game.GameAdapter
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class AllGameFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameAllBinding.inflate(inflater)
        val viewModel: AllGameViewModel by viewModels()
        viewModel.games.observe(viewLifecycleOwner,{
            binding.rvAllGame.adapter = GameAdapter(1).apply {
                submitList(it)
            }
        })


        return binding.root
    }

}