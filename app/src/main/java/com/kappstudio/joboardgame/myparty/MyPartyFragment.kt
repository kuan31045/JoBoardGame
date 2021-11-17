package com.kappstudio.joboardgame.myparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.databinding.FragmentMyPartyBinding
import com.kappstudio.joboardgame.party.PartyAdapter
import com.kappstudio.joboardgame.party.PartyViewModel
import timber.log.Timber

class MyPartyFragment : Fragment() {

    lateinit var partyViewModel: PartyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        partyViewModel = ViewModelProvider(requireParentFragment()).get(PartyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyPartyBinding.inflate(inflater)

        partyViewModel.parties.observe(viewLifecycleOwner, {
            Timber.d("completedData $it")
            binding.rvParty.adapter = PartyAdapter(partyViewModel).apply {
                submitList(it.filter {
                    UserManager.user.value?.id ?: "" in it.playerIdList
                })
            }
        })

        partyViewModel.navToPartyDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(MyPartyFragmentDirections.navToPartyDetailFragment(it))
                partyViewModel.onNavToPartyDetail()
            }
        })

        return binding.root
    }

}