package com.kappstudio.joboardgame.myrating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentMyRatingBinding
import com.kappstudio.joboardgame.gamedetail.GameDetailFragmentDirections

class MyRatingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyRatingBinding.inflate(inflater)
        val viewModel: MyRatingViewModel by viewModels()

        viewModel.games.observe(viewLifecycleOwner, {
            binding.rvGame.adapter = RatingAdapter(viewModel).apply{
                submitList(
                    it
                )
            }
        })
        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(MyRatingFragmentDirections.navToGameDetailFragment(it))
                viewModel.onNavToGameDetail()
            }
        })
        viewModel.navToRating.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(GameDetailFragmentDirections.navToRatingDialog(it))
                viewModel.onNavToRating()
            }
        })
        return binding.root
    }

}