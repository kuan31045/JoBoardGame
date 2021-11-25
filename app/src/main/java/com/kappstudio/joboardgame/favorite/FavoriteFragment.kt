package com.kappstudio.joboardgame.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.databinding.FragmentFavoriteBinding
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.party.PartyViewModel
import com.kappstudio.joboardgame.user.UserFragmentArgs
import com.kappstudio.joboardgame.user.UserViewModel


class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater)


        val userViewModel: UserViewModel by viewModels {
            VMFactory {
                UserViewModel(
                    FavoriteFragmentArgs.fromBundle(requireArguments()).userId,
                )
            }
        }


        val viewModel: FavoriteViewModel by viewModels()

        val adapter = FavoriteAdapter(
            viewModel,
            FavoriteFragmentArgs.fromBundle(requireArguments()).userId == UserManager.user.value?.id ?: "",
        )

        binding.rvGame.adapter = adapter
        userViewModel.user.observe(viewLifecycleOwner, {
            adapter.submitList(it.favoriteGames)
            when (it.favoriteGames.size) {
                0 -> {
                    binding.tvNotFound.visibility = View.VISIBLE
                    binding.lottieNotFound.visibility = View.VISIBLE
                }
                else -> {
                    binding.tvNotFound.visibility = View.GONE
                    binding.lottieNotFound.visibility = View.GONE
                }
            }
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