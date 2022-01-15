package com.kappstudio.joboardgame.ui.myhost

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
import com.kappstudio.joboardgame.databinding.FragmentMyHostBinding
import com.kappstudio.joboardgame.ui.myparty.MyPartyFragmentDirections
import com.kappstudio.joboardgame.ui.party.PartyAdapter

class MyHostFragment : Fragment() {

    val viewModel: MyHostViewModel by viewModels {
        VMFactory {
            MyHostViewModel(
                MyHostFragmentArgs.fromBundle(requireArguments()).userId,
                appInstance.provideJoRepository()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMyHostBinding.inflate(inflater)
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