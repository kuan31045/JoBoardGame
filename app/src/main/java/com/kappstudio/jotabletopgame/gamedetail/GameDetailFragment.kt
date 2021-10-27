package com.kappstudio.jotabletopgame.gamedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.bindImage
import com.kappstudio.jotabletopgame.bindTextViewGameTypes
import com.kappstudio.jotabletopgame.databinding.FragmentGameDetailBinding
import timber.log.Timber


class GameDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameDetailBinding.inflate(inflater)
        val viewModel: GameDetailViewModel by viewModels {
            VMFactory {
                GameDetailViewModel(
                    GameDetailFragmentArgs.fromBundle(requireArguments()).clickedGameId,
                )
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.game.observe(viewLifecycleOwner, {
            Timber.d("game= $it")
            bindImage(binding.ivGame, it.image)
            it.type?.let { it1 -> bindTextViewGameTypes(binding.tvType, it1) }
        })


        return binding.root
    }


}