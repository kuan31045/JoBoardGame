package com.kappstudio.joboardgame.ui.gamedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentGameDetailBinding
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.bindTextViewGameTypes
import org.koin.core.parameter.parametersOf
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameDetailFragment : Fragment() {

    private val viewModel: GameDetailViewModel by viewModel {
        parametersOf(
            GameDetailFragmentArgs.fromBundle(
                requireArguments()
            ).clickedGameId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentGameDetailBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val transition: Transition = Fade()
        transition.duration = 600
        transition.addTarget(binding.ivGame)
        if (container != null) {
            TransitionManager.beginDelayedTransition(container, transition)
        }

        binding.tvDesc.setOnClickListener {
            binding.tvDesc.text = viewModel.game.value?.desc ?: ""
        }

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        binding.btnToBottle.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.navToBottleFragment())
        }

        binding.btnToDice.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.navToDiceFragment())
        }

        binding.btnToTimer.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.navToTimerFragment())
        }

        binding.btnToScoreboard.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.navToScoreboardFragment())
        }

        binding.btnToDrawlots.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.navToDrawLotsFragment())
        }

        binding.btnToPolygraph.setOnClickListener {
            findNavController().navigate(GameDetailFragmentDirections.navToPolygraphFragment())
        }

        viewModel.game.observe(viewLifecycleOwner) {
            it?.let {
                bindImage(binding.ivGame, it.image)
                bindTextViewGameTypes(binding.tvType, it.type)

                if (it.desc.length > 100) {
                    binding.tvDesc.text = it.desc.substring(0..100) + "···"
                } else {
                    binding.tvDesc.text = it.desc
                }

                viewModel.addViewedGame()

                if (it.tools.isNotEmpty()) {
                    binding.tvTitleTools.visibility = View.VISIBLE
                } else {
                    binding.tvTitleTools.visibility = View.GONE
                }
            }
        }

        viewModel.navToRating.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(GameDetailFragmentDirections.navToRatingDialog(it))
                viewModel.onNavToRating()
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) {
            binding.cbFavorite.isChecked = it
        }

        return binding.root
    }
}