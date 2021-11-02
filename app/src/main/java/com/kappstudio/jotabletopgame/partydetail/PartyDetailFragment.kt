package com.kappstudio.jotabletopgame.partydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration

import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.appInstance
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
        binding.rvMsg.addItemDecoration(
            DividerItemDecoration(
                appInstance, DividerItemDecoration.VERTICAL
            )
        )
        viewModel.party.observe(viewLifecycleOwner, {
            binding.rvPlayer.adapter = PlayerAdapter(viewModel).apply {
                submitList(it?.playerList)
            }
            binding.rvPartyGame.adapter = GameAdapter(viewModel).apply {
                submitList(it?.gameList)
            }

        })



        viewModel.partyMsgs.observe(viewLifecycleOwner, {
            binding.rvMsg.adapter = PartyMsgAdapter(viewModel).apply {
                submitList(it.sortedByDescending { it.createdTime })
            }
        })


        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(
                    PartyDetailFragmentDirections.navToGameDetailFragment(
                        it
                    )
                )
                viewModel.onNavToGameDetail()
            }
        })

        viewModel.navToUser.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToUserDialog(it))
                viewModel.onNavToUser()
            }
        })



        return binding.root
    }


}