package com.kappstudio.joboardgame.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.factory.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    val viewModel: GameViewModel by viewModels {
        VMFactory {
            GameViewModel(appInstance.provideJoRepository())
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGameBinding.inflate(inflater)
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
