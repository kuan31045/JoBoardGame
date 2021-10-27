package com.kappstudio.jotabletopgame.partydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.jotabletopgame.game.GameAdapter
import timber.log.Timber

class PartyDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPartyDetailBinding.inflate(inflater)
        val viewModel: PartyDetailViewModel by viewModels {
            VMFactory {
                PartyDetailViewModel(
                    PartyDetailFragmentArgs.fromBundle(requireArguments()).clickedPartyId,
                )
            }
        }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.party.observe(viewLifecycleOwner, {
            binding.rvPlayer.adapter = PlayerAdapter().apply {
                submitList(it?.playerList)
            }
        })

        viewModel.games.observe(viewLifecycleOwner, {
            Timber.d("Games = $it")
            binding.rvPartyGame.adapter = GameAdapter(viewModel).apply {
                submitList(it)
            }
        })

        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToGameDetailFragment(it))
                viewModel.onNavToGameDetail()
            }
        })

        return binding.root
    }


}