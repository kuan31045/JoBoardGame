package com.kappstudio.joboardgame.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.google.firebase.auth.FirebaseAuth
import com.kappstudio.joboardgame.*
import com.kappstudio.joboardgame.databinding.FragmentProfileBinding
import com.kappstudio.joboardgame.game.GameAdapter
import com.kappstudio.joboardgame.login.LoginActivity
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.myhost.MyHostFragmentDirections
import com.kappstudio.joboardgame.party.PartyAdapter
import com.kappstudio.joboardgame.party.PartyFragmentDirections
 import com.kappstudio.joboardgame.partydetail.PartyDetailFragmentDirections
import com.kappstudio.joboardgame.user.UserFragmentDirections
import com.kappstudio.joboardgame.util.closeKeyBoard
import timber.log.Timber
import java.util.*

class ProfileFragment : Fragment() {

    val viewModel: ProfileViewModel by viewModels {
        VMFactory {
            ProfileViewModel(
                appInstance.provideJoRepository()
            )
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater)

        val userId = UserManager.user.value?.id ?: ""

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel


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

        viewModel.viewedGames.observe(viewLifecycleOwner, {
            val list = if (it.size > 20) {
                it.slice(0..19) //最近瀏覽20款
            } else {
                it
            }
            Timber.d("Set viewed games:$list")
            binding.rvGame.adapter = GameAdapter(viewModel).apply {
                submitList(list)
            }
        })
        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(ProfileFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
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

    override fun onResume() {
        super.onResume()
       viewModel.getUserInfo()
    }

}


