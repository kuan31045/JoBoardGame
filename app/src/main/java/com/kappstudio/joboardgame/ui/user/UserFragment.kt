package com.kappstudio.joboardgame.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.factory.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentUserBinding

import com.kappstudio.joboardgame.ui.myhost.MyHostFragmentDirections
import com.kappstudio.joboardgame.ui.profile.ProfileFragmentDirections


class UserFragment : Fragment() {

    val viewModel: UserViewModel by viewModels {
        VMFactory {
            UserViewModel(
                UserFragmentArgs.fromBundle(requireArguments()).clickedUserId,
                appInstance.provideJoRepository()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentUserBinding.inflate(inflater, container, false)
        val userId = UserFragmentArgs.fromBundle(requireArguments()).clickedUserId


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

//        viewModel.comingParties.observe(viewLifecycleOwner,
//            {
//                binding.rvParty.adapter = PartyAdapter(viewModel, appInstance.provideJoRepository()).apply {
//                    submitList(
//                        it
//                    )
//                }
//            })

        viewModel.navToPartyDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(UserFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        })

        viewModel.navToReport.observe(viewLifecycleOwner,{
            it?.let {
                findNavController().navigate(UserFragmentDirections.navToReportDialog(it))
                viewModel.onNavToReport()
            }
        })

        return binding.root
    }

}