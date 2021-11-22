package com.kappstudio.joboardgame.party

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.allParties
import com.kappstudio.joboardgame.allUsers
import com.kappstudio.joboardgame.databinding.FragmentPartyBinding
import timber.log.Timber

class PartyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPartyBinding.inflate(inflater)
        val viewModel: PartyViewModel by viewModels()

        binding.btnNewParty.setOnClickListener {
            findNavController().navigate(PartyFragmentDirections.navToNewPartyFragment())
        }


        viewModel.parties.observe(viewLifecycleOwner, {
            it?.let{
                binding.rvParty.adapter = PartyAdapter(viewModel).apply {
                    submitList(it)
                }
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