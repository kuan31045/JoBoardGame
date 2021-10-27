package com.kappstudio.jotabletopgame.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.databinding.FragmentGameBinding

class GameFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGameBinding.inflate(inflater)
        val viewModel: GameViewModel by viewModels()

        viewModel.games.observe(viewLifecycleOwner, {
            binding.rvAllGame.adapter = GameAdapter(viewModel).apply {
                submitList(it)
            }
        })

        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(GameFragmentDirections.navToGameDetailFragment(it))
                viewModel.onNavToGameDetail()
            }
        })
        return binding.root
    }
}
