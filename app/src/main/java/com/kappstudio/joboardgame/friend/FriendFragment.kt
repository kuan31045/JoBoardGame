package com.kappstudio.joboardgame.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.databinding.FragmentFriendBinding
import com.kappstudio.joboardgame.search.UserAdapter

class FriendFragment : Fragment() {

    val viewModel: FriendViewModel by viewModels {
        VMFactory {
            FriendViewModel(
                FriendFragmentArgs.fromBundle(requireArguments()).userId,
                appInstance.provideJoRepository(),
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentFriendBinding.inflate(inflater, container, false)
        val adapter = UserAdapter(viewModel)

        binding.rvFriend.adapter = adapter

        viewModel.user.observe(viewLifecycleOwner, { user ->
            if (user.friendList.isNotEmpty()) {
                viewModel.getFriends()

                viewModel.friends.observe(viewLifecycleOwner, { friends ->
                    adapter.submitList(friends)
                })
            }
            bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, user.friendList)
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