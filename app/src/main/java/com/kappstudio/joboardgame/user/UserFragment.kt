package com.kappstudio.joboardgame.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.databinding.FragmentUserBinding
import com.kappstudio.joboardgame.party.PartyAdapter
import com.kappstudio.joboardgame.party.PartyFragmentDirections
import java.util.*


class UserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater, container, false)
        val userId = UserFragmentArgs.fromBundle(requireArguments()).clickedUserId

        val viewModel: UserViewModel by viewModels {
            VMFactory {
                UserViewModel(
                    UserFragmentArgs.fromBundle(requireArguments()).clickedUserId,
                )
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        //Nav
        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        binding.tvFriendQty.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.navToFriendFragment(userId))
        }
        binding.tvFavorite.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.navToFavoriteFragment(userId))
        }
        binding.tvRating.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.navToMyRatingFragment())
        }



        viewModel.me.observe(viewLifecycleOwner, {
            viewModel.reUser()
        })

        viewModel.user.observe(viewLifecycleOwner, {
            viewModel.checkFriendStatus()
        })

        viewModel.parties.observe(viewLifecycleOwner,
            {
                binding.rvParty.adapter = PartyAdapter(viewModel).apply {
                    submitList(
                        it.filter {
                            it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis
                        }
                    )
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