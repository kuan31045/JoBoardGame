package com.kappstudio.joboardgame.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.databinding.FragmentFavoriteBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteFragment : Fragment() {

    private val viewModel: FavoriteViewModel by viewModel {
        parametersOf(
            FavoriteFragmentArgs.fromBundle(requireArguments()).userId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentFavoriteBinding.inflate(inflater)
        val adapter = FavoriteAdapter(
            viewModel
        )

        binding.rvGame.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner) {
            adapter.submitList(it.favoriteGames)
            bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, it.favoriteGames)
        }

        viewModel.navToGameDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(FavoriteFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        }

        return binding.root
    }
}