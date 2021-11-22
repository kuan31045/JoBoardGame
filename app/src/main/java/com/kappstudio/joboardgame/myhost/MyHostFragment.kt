package com.kappstudio.joboardgame.myhost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentMyHostBinding
import com.kappstudio.joboardgame.databinding.FragmentMyPartyBinding
import com.kappstudio.joboardgame.myparty.MyPartyFragmentArgs
import com.kappstudio.joboardgame.myparty.MyPartyFragmentDirections
import com.kappstudio.joboardgame.party.PartyAdapter
import com.kappstudio.joboardgame.party.PartyViewModel
import timber.log.Timber


class MyHostFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyHostBinding.inflate(inflater)
        val viewModel: PartyViewModel by viewModels()

        val userId = MyHostFragmentArgs.fromBundle(requireArguments()).userId
        viewModel.parties?.observe(viewLifecycleOwner, {
            Timber.d("completedData $it")
            val list = it.filter {
                userId == it.hostId
            }
            binding.rvParty.adapter = PartyAdapter(viewModel).apply {
                submitList(list)
            }

            binding.lottieNotFound.visibility = when (list.size) {
                0 -> View.VISIBLE
                else -> View.GONE
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

}