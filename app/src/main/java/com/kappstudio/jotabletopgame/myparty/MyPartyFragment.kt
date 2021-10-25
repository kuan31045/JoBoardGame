package com.kappstudio.jotabletopgame.myparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.databinding.FragmentMyPartyBinding
import com.kappstudio.jotabletopgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.jotabletopgame.home.HomeFragmentDirections
import com.kappstudio.jotabletopgame.home.PartyAdapter
import com.kappstudio.jotabletopgame.partydetail.PartyDetailFragmentArgs
import com.kappstudio.jotabletopgame.partydetail.PartyDetailViewModel
import timber.log.Timber

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