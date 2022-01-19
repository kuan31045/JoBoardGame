package com.kappstudio.joboardgame.ui.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.factory.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.databinding.FragmentFavoriteBinding
import com.kappstudio.joboardgame.ui.login.UserManager

class FavoriteFragment : Fragment() {

    val viewModel: FavoriteViewModel by viewModels {
        VMFactory {
            FavoriteViewModel(
                FavoriteFragmentArgs.fromBundle(requireArguments()).userId,
                appInstance.provideJoRepository(),
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater)
        val adapter = FavoriteAdapter(
            viewModel,
            FavoriteFragmentArgs.fromBundle(requireArguments()).userId
                    == UserManager.user.value?.id ?: "",
        )

        binding.rvGame.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner, {
            adapter.submitList(it.favoriteGames)
            bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, it.favoriteGames)
        })

        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(FavoriteFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        })

        return binding.root
    }
}