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
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.databinding.FragmentProfileBinding
import com.kappstudio.joboardgame.game.GameAdapter
import com.kappstudio.joboardgame.login.LoginActivity
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.partydetail.PartyDetailFragmentDirections
import com.kappstudio.joboardgame.util.closeKeyBoard
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
         }

        viewModel.user.observe(viewLifecycleOwner, {
            bindImage(binding.ivProfile, it.image)
        })

        binding.tvLogout.setOnClickListener {
            UserManager.clear()
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(appInstance, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }



        binding.etUserStatus.setOnClickListener {
            val builder: AlertDialog.Builder = android.app.AlertDialog.Builder(context)
            builder.setTitle(getString(R.string.my_status))

            // Set up the input
            val input = EditText(context)
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setHint(getString(R.string.i_think))
            input.gravity = 0

            input.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE
            input.minLines = 3
            builder.setView(input)

            // Set up the buttons
            builder.setPositiveButton(
                getString(R.string.send),
                DialogInterface.OnClickListener { dialog, which ->
                    // Here you get get input text from the Edittext
                    viewModel.setStatus(
                        input.text.toString()
                    )
                    activity?.let { it1 -> closeKeyBoard(it1) }


                })

            builder.setNegativeButton(
                getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, which ->
                    dialog.cancel()
                    activity?.let { it1 -> closeKeyBoard(it1) }
                })

            builder.show()
        }


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
                findNavController().navigate(ProfileFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        })


        return binding.root
    }


}


