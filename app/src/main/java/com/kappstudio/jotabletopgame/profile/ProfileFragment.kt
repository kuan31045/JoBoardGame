package com.kappstudio.jotabletopgame.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.bindImage
import com.kappstudio.jotabletopgame.databinding.FragmentProfileBinding
import com.kappstudio.jotabletopgame.game.GameAdapter
import com.kappstudio.jotabletopgame.game.GameFragmentDirections
import timber.log.Timber

class ProfileFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)
        val viewModel: ProfileViewModel by viewModels {
            VMFactory {
                ProfileViewModel(
                    appInstance.provideJoRepository()
                )
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.btnMyParty.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToMyPartyFragment())
        }

        viewModel.user.observe(viewLifecycleOwner, {
            bindImage(binding.ivProfile, it.image)
        })

        viewModel.viewedGames.observe(viewLifecycleOwner, {
            val list = if (it.size > 20) {
                it.slice(0..19) //最近瀏覽20款
            } else {
                it
            }
            Timber.d("Set viewed games:$list")
            binding.rvRecentlyViewedGame.adapter = GameAdapter(viewModel).apply {
                submitList(list)
            }
        })

        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navToGameDetailFragment(it))
                viewModel.onNavToGameDetail()
            }
        })

        return binding.root
    }
}


