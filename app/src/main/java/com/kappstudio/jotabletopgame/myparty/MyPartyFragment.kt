package com.kappstudio.jotabletopgame.myparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.databinding.FragmentMyPartyBinding
import com.kappstudio.jotabletopgame.party.PartyAdapter

class MyPartyFragment : Fragment() {
    val viewModel: MyPartyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyPartyBinding.inflate(inflater)

        viewModel.parties.observe(viewLifecycleOwner, {
            binding.rvParty.adapter = PartyAdapter(viewModel).apply {
                submitList(
                it.sortedByDescending { it.partyTime }
            )
            }
        })

        viewModel.navToPartyDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(MyPartyFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyParties()
    }
}