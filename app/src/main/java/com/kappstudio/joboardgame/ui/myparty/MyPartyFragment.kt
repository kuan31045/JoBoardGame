package com.kappstudio.joboardgame.ui.myparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.factory.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.databinding.FragmentMyPartyBinding
import com.kappstudio.joboardgame.ui.party.PartyAdapter

class MyPartyFragment : Fragment() {

    val viewModel: MyPartyViewModel by viewModels {
        VMFactory {
            MyPartyViewModel(
                MyPartyFragmentArgs.fromBundle(requireArguments()).userId,
                appInstance.provideJoRepository()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyPartyBinding.inflate(inflater)
        val adapter = PartyAdapter(viewModel)

        binding.rvParty.adapter = adapter

        viewModel.parties.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                viewModel.getHosts()

                viewModel.hosts.observe(viewLifecycleOwner, {
                    adapter.submitList(viewModel.parties.value)
                })
            }
            bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, it)
        })

        viewModel.navToPartyDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(MyPartyFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        })

        return binding.root
    }
}