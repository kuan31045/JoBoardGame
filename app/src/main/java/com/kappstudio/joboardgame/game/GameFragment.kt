package com.kappstudio.joboardgame.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentGameBinding
import com.kappstudio.joboardgame.isConnect
import com.kappstudio.joboardgame.party.PartyFragmentDirections

class GameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGameBinding.inflate(inflater)
        val viewModel: GameViewModel by viewModels()

        binding.btnNewGame.visibility = when (isConnect.value) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        val adapter = AllGameAdapter(viewModel)
        binding.rvAllGame.adapter = adapter



        binding.btnNewGame.setOnClickListener {
            findNavController().navigate(GameFragmentDirections.navToNewGameFragment(""))
        }
        viewModel.games.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(GameFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        })
        return binding.root
    }
}
