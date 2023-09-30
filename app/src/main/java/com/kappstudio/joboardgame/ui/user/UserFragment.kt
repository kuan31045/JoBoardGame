package com.kappstudio.joboardgame.ui.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentUserBinding
import com.kappstudio.joboardgame.ui.myhost.MyHostFragmentDirections
import com.kappstudio.joboardgame.ui.profile.ProfileFragmentDirections
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.util.ToastUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class UserFragment : Fragment() {

    private val viewModel: UserViewModel by viewModel {
        parametersOf(
            UserFragmentArgs.fromBundle(requireArguments()).clickedUserId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentUserBinding.inflate(inflater, container, false)
        val userId = UserFragmentArgs.fromBundle(requireArguments()).clickedUserId

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        //Nav
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

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

        binding.btnMore.setOnClickListener {
            val popMenu = PopupMenu(appInstance, binding.btnMore)
            popMenu.menuInflater.inflate(R.menu.pop_user_menu, popMenu.menu)
            popMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.report_user -> viewModel.reportUser()

                    R.id.block_user -> {
                        ToastUtil.show("封鎖已送出")
                        val mAlert = android.app.AlertDialog.Builder(activity)
                        mAlert.setTitle("封鎖使用者")
                        mAlert.setMessage(getString(R.string.block))
                        mAlert.setCancelable(false)
                        mAlert.setPositiveButton("確定") { _, _ ->
                        }
                        val mAlertDialog = mAlert.create()
                        mAlertDialog.show()
                    }

                    R.id.see_rule -> {
                        val mAlert = android.app.AlertDialog.Builder(activity)
                        mAlert.setTitle("社群規範")
                        mAlert.setMessage(getString(R.string.rule))
                        mAlert.setCancelable(false)
                        mAlert.setPositiveButton("確定") { _, _ ->
                        }
                        val mAlertDialog = mAlert.create()
                        mAlertDialog.show()
                    }
                }
                true
            }
            popMenu.show()
        }

        viewModel.user.observe(viewLifecycleOwner) {
            viewModel.checkFriendStatus()
        }

//        viewModel.comingParties.observe(viewLifecycleOwner,
//            {
//                binding.rvParty.adapter = PartyAdapter(viewModel, appInstance.provideJoRepository()).apply {
//                    submitList(
//                        it
//                    )
//                }
//            })

        viewModel.navToPartyDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(UserFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        }

        viewModel.hasReported.observe(viewLifecycleOwner) {
            it?.let {
                val mAlert = android.app.AlertDialog.Builder(activity)
                mAlert.setTitle(getString(R.string.report_ok))
                mAlert.setMessage(getString(R.string.google_play_want_see_this))
                mAlert.setCancelable(true)
                mAlert.setPositiveButton(getString(R.string.ok)) { _, _ ->
                }

                val mAlertDialog = mAlert.create()
                mAlertDialog.show()
                viewModel.hasReported.value = null
            }
        }

        return binding.root
    }
}