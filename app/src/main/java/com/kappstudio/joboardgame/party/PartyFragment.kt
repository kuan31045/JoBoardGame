package com.kappstudio.joboardgame.party

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentPartyBinding

class PartyFragment : Fragment() {

    val viewModel: PartyViewModel by viewModels {
        VMFactory {
            PartyViewModel(appInstance.provideJoRepository())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPartyBinding.inflate(inflater)
        val adapter = PartyAdapter(viewModel)

        binding.rvParty.adapter = adapter

        binding.btnNewParty.setOnClickListener {
            findNavController().navigate(PartyFragmentDirections.navToNewPartyFragment())
        }

        viewModel.parties.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                viewModel.getHosts()

                viewModel.hosts.observe(viewLifecycleOwner, {
                    adapter.submitList(viewModel.parties.value)
                })
            }
        })

        viewModel.navToPartyDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        })

        return binding.root
    }
}