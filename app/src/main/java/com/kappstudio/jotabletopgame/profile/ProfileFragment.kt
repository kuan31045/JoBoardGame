package com.kappstudio.jotabletopgame.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)

        binding.btnMyParty.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToMyPartyFragment())
        }

        return binding.root
    }}


