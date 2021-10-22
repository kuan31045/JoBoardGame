package com.kappstudio.jotabletopgame.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.databinding.FragmentGameAllBinding
import com.kappstudio.jotabletopgame.databinding.FragmentHomeBinding
import com.kappstudio.jotabletopgame.game.GameAdapter
import com.kappstudio.jotabletopgame.game.all.AllGameViewModel
import timber.log.Timber

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        val viewModel: HomeViewModel by viewModels()

        viewModel.parties.observe(viewLifecycleOwner, {
            Timber.d("$it")
            binding.rvParty.adapter = PartyAdapter(viewModel).apply {
                submitList(it)
            }
        })

        viewModel.navToPartyDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(HomeFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        })


        return binding.root
    }

}