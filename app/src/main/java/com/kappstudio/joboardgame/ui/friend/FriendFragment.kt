package com.kappstudio.joboardgame.ui.friend

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.databinding.FragmentFriendBinding
import com.kappstudio.joboardgame.ui.search.UserAdapter
import com.kappstudio.joboardgame.util.ToastUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FriendFragment : Fragment() {

    private val viewModel: FriendViewModel by viewModel {
        parametersOf(
            FriendFragmentArgs.fromBundle(requireArguments()).userId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentFriendBinding.inflate(inflater, container, false)
        val adapter = UserAdapter(viewModel)

        binding.rvFriend.adapter = adapter

        viewModel.friends.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val friends = result.data
                    adapter.submitList(result.data)
                    bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, friends)
                    binding.lottieLoading.visibility = View.GONE
                }

                is Result.Loading -> binding.lottieLoading.visibility = View.VISIBLE

                else -> {
                    ToastUtil.show(getString(R.string.cant_get_friend))
                    binding.lottieLoading.visibility = View.GONE
                }
            }
        }

        viewModel.navToUser.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(FriendFragmentDirections.navToUserFragment(it))
                viewModel.onNavToUser()
            }
        }

        return binding.root
    }
}