package com.kappstudio.joboardgame.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.allUsers
import com.kappstudio.joboardgame.databinding.FragmentFriendBinding
import com.kappstudio.joboardgame.partydetail.PartyDetailFragmentDirections
import com.kappstudio.joboardgame.search.UserAdapter
import com.kappstudio.joboardgame.user.UserViewModel

class FriendFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFriendBinding.inflate(inflater, container, false)

        val viewModel: UserViewModel by viewModels {
            VMFactory {
                UserViewModel(
                    FriendFragmentArgs.fromBundle(requireArguments()).userId,
                )
            }
        }

        val adapter = UserAdapter(viewModel)
        binding.rvFriend.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner, { user ->
            adapter.submitList(allUsers.value?.filter { user.friendList.contains(it.id) }
            )
            binding.lottieNotFound.visibility = when (user.friendList.size) {
                0 -> View.VISIBLE
                else -> View.GONE
            }
        })
        viewModel.navToUser.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(FriendFragmentDirections.navToUserFragment(it))
                viewModel.onNavToUser()
            }
        })

        return binding.root
    }


}