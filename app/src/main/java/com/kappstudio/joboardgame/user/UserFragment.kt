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
import com.kappstudio.joboardgame.myhost.MyHostFragmentDirections
import com.kappstudio.joboardgame.party.PartyAdapter
import com.kappstudio.joboardgame.party.PartyFragmentDirections
import com.kappstudio.joboardgame.profile.ProfileFragmentDirections
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



        binding.tvPartyQty.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.navToMyPartyFragment(userId))
        }
        binding.tvHostQty.setOnClickListener {
          findNavController().navigate(MyHostFragmentDirections.navToMyHostFragment(userId))
        }
        binding.tvFriendQty.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.navToFriendFragment(userId))
        }
        binding.tvFavorite.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.navToFavoriteFragment(userId))
        }
        binding.tvRating.setOnClickListener {
            findNavController().navigate(UserFragmentDirections.navToMyRatingFragment(userId))
        }
        binding.tvPhoto.setOnClickListener {
            viewModel.user.value?.photos?.let {
                findNavController().navigate(
                    ProfileFragmentDirections.navToAlbumFragment(
                        it.toTypedArray()
                    )
                )
            }
        }






        viewModel.me.observe(viewLifecycleOwner, {
            viewModel.reUser()
        })

        viewModel.user.observe(viewLifecycleOwner, {
            viewModel.checkFriendStatus()
        })

        viewModel.comingParties.observe(viewLifecycleOwner,
            {
                binding.rvParty.adapter = PartyAdapter(viewModel).apply {
                    submitList(
                        it
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