package com.kappstudio.joboardgame.ui.myrating

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.databinding.FragmentMyRatingBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyRatingFragment : Fragment() {

    private val viewModel: MyRatingViewModel by viewModel {
        parametersOf(
            MyRatingFragmentArgs.fromBundle(requireArguments()).userId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentMyRatingBinding.inflate(inflater)
        val adapter = RatingAdapter(viewModel)

        binding.rvGame.adapter = adapter

        viewModel.ratings.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, it)
        }

        viewModel.navToGameDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(MyRatingFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        }

        return binding.root
    }
}