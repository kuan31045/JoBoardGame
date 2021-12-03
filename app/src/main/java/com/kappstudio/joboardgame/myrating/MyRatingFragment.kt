package com.kappstudio.joboardgame.myrating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.databinding.FragmentMyRatingBinding

class MyRatingFragment : Fragment() {

    val viewModel: MyRatingViewModel by viewModels {
        VMFactory {
            MyRatingViewModel(
                MyRatingFragmentArgs.fromBundle(requireArguments()).userId,
                appInstance.provideJoRepository()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyRatingBinding.inflate(inflater)
        val adapter = RatingAdapter(viewModel)

        binding.rvGame.adapter = adapter

        viewModel.ratings.observe(viewLifecycleOwner, {
             adapter.submitList(it)
            bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, it)
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