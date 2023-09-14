package com.kappstudio.joboardgame.ui.party

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.databinding.FragmentPartyBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PartyFragment : Fragment() {

    val viewModel: PartyViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentPartyBinding.inflate(inflater)
        val adapter = PartyAdapter(viewModel)

        binding.rvParty.adapter = adapter

        binding.btnNewParty.setOnClickListener {
            findNavController().navigate(PartyFragmentDirections.navToNewPartyFragment())
        }

        viewModel.parties.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                viewModel.getHosts()

                viewModel.hosts.observe(viewLifecycleOwner) {
                    adapter.submitList(viewModel.parties.value)
                    viewModel.status.value = LoadApiStatus.DONE
                }
            } else {
                viewModel.status.value = LoadApiStatus.DONE
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            binding.lottieLoading.visibility = if (it == LoadApiStatus.LOADING) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        viewModel.navToPartyDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(PartyFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        }

        return binding.root
    }
}