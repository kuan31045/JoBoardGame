package com.kappstudio.jotabletopgame.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.databinding.FragmentFavoriteBinding


class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater)
        val viewModel: FavoriteViewModel by viewModels()
val adapter =  FavoriteAdapter(viewModel)
        binding.rvGame.adapter = adapter
        viewModel.games.observe(viewLifecycleOwner, {
           adapter.submitList(it)



        })

        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(FavoriteFragmentDirections.navToGameDetailFragment(it))
                viewModel.onNavToGameDetail()
            }
        })



        return binding.root
    }


}