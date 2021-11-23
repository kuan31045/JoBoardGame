package com.kappstudio.joboardgame.myparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.databinding.FragmentMyPartyBinding
import com.kappstudio.joboardgame.party.PartyAdapter
import com.kappstudio.joboardgame.party.PartyViewModel
import com.kappstudio.joboardgame.user.UserFragmentArgs
import timber.log.Timber

class MyPartyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyPartyBinding.inflate(inflater)
        val viewModel: PartyViewModel by viewModels()

        val userId = MyPartyFragmentArgs.fromBundle(requireArguments()).userId

        viewModel.parties?.observe(viewLifecycleOwner, {
            Timber.d("completedData $it")
            val list = it.filter {
                userId in it.playerIdList
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