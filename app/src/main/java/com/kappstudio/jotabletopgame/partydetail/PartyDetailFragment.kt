package com.kappstudio.jotabletopgame.partydetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.data.Game
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

        var games: MutableList<Game> = mutableListOf()
        repeat(13) {
            games.add(
                Game(
                    name = "卡坦島$it"
                )
            )
        }
        viewModel.host.observe(viewLifecycleOwner, {
            it?.let {
                Timber.d("gethost ${it.name}")
            }
        })
        viewModel.party.observe(viewLifecycleOwner,{
            viewModel.setHost()
        })

        binding.rvPartyGame.adapter = GameAdapter(GameAdapterType.SIMPLE).apply {
            submitList(games)
        }
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        return binding.root
    }


}