package com.kappstudio.jotabletopgame.myparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.databinding.FragmentMyPartyBinding
import com.kappstudio.jotabletopgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.jotabletopgame.partydetail.PartyDetailFragmentArgs
import com.kappstudio.jotabletopgame.partydetail.PartyDetailViewModel
import timber.log.Timber

class MyPartyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyPartyBinding.inflate(inflater)
        val viewModel: MyPartyViewModel by viewModels()
        viewModel.parties.observe(viewLifecycleOwner,{
            Timber.d("parties= $it")
        })
        return binding.root
    }
}