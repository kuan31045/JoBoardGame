package com.kappstudio.joboardgame.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentProfileBinding
import com.kappstudio.joboardgame.ui.game.GameAdapter
import com.kappstudio.joboardgame.ui.login.LoginActivity
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.ui.party.PartyAdapter
import com.kappstudio.joboardgame.ui.party.PartyFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentProfileBinding.inflate(inflater)
        val userId = UserManager.user.value?.id ?: ""
        val partyAdapter = PartyAdapter(viewModel)
        val gameAdapter = GameAdapter(viewModel)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        binding.rvParty.adapter = partyAdapter
        binding.rvGame.adapter = gameAdapter

        binding.tvLogout.setOnClickListener {
            UserManager.clear()
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(appInstance, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.tvPartyQty.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToMyPartyFragment(userId))
        }

        binding.tvHostQty.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToMyHostFragment(userId))
        }

        binding.tvFriendQty.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToFriendFragment(userId))
        }

        binding.tvFavorite.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToFavoriteFragment(userId))
        }

        binding.tvRating.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.navToMyRatingFragment(userId))
        }

        binding.tvPhoto.setOnClickListener {
            viewModel.me.value?.photos?.let {
                findNavController().navigate(
                    ProfileFragmentDirections.navToAlbumFragment(
                        it.toTypedArray()
                    )
                )
            }
        }

        viewModel.comingParties.observe(viewLifecycleOwner) {
            partyAdapter.submitList(it)
        }

        viewModel.viewedGames.observe(viewLifecycleOwner) {
            binding.rvGame.adapter = GameAdapter(viewModel).apply {
                submitList(if (it.size > 20) it.slice(0..19) else it)
            }
        }

        viewModel.navToPartyDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(PartyFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        }

        viewModel.navToGameDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        }

        return binding.root
    }
}