package com.kappstudio.jotabletopgame.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.bindImage
import com.kappstudio.jotabletopgame.databinding.FragmentProfileBinding
import com.kappstudio.jotabletopgame.game.GameViewModel

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)
        val viewModel: ProfileViewModel by viewModels()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.btnMyParty.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToMyPartyFragment())
        }

        viewModel.user.observe(viewLifecycleOwner,{
            bindImage(binding.ivProfile,it.image)
        })

        return binding.root
    }
}


