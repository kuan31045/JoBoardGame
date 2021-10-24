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
import com.kappstudio.jotabletopgame.game.GameAdapterType
import tech.gujin.toast.ToastUtil
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
                    PartyDetailFragmentArgs.fromBundle(requireArguments()).selectedPartyId,
                )
            }
        }

        binding.lifecycleOwner = this
        binding.vm = viewModel




        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.games.observe(viewLifecycleOwner,{
            Timber.d("Games = $it")
            binding.rvPartyGame.adapter = GameAdapter(GameAdapterType.SIMPLE).apply {
                submitList(it)
            }
        })

        return binding.root
    }


}