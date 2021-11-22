package com.kappstudio.joboardgame.myrating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.databinding.FragmentMyRatingBinding
import com.kappstudio.joboardgame.favorite.FavoriteFragmentArgs
import com.kappstudio.joboardgame.gamedetail.GameDetailFragmentDirections
import com.kappstudio.joboardgame.user.UserViewModel

class MyRatingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyRatingBinding.inflate(inflater)

        val viewModel: MyRatingViewModel by viewModels {
            VMFactory {
                MyRatingViewModel(
                    MyRatingFragmentArgs.fromBundle(requireArguments()).userId,
                )
            }
        }

        viewModel.games.observe(viewLifecycleOwner, {
            binding.rvGame.adapter = RatingAdapter(viewModel).apply{
                submitList(
                    it
                )
            }
            binding.lottieNotFound.visibility = when (it.size) {
                0 -> View.VISIBLE
                else -> View.GONE
            }
        })
        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(MyRatingFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        })
        return binding.root
    }

}