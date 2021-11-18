package com.kappstudio.joboardgame.partydetail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.github.dhaval2404.imagepicker.ImagePicker
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kappstudio.joboardgame.R

import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.allGames
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.joboardgame.game.GameAdapter
import com.kappstudio.joboardgame.game.GameFragmentDirections
import com.kappstudio.joboardgame.party.PhotoAdapter
import com.kappstudio.joboardgame.util.closeSoftKeyboard
import tech.gujin.toast.ToastUtil

class PartyDetailFragment : Fragment() {
    lateinit var binding: FragmentPartyDetailBinding
    lateinit var startActivityLauncher: StartActivityLauncher
    val viewModel: PartyDetailViewModel by viewModels {
        VMFactory {
            PartyDetailViewModel(
                PartyDetailFragmentArgs.fromBundle(requireArguments()).clickedPartyId
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        startActivityLauncher = StartActivityLauncher(this)
        binding = FragmentPartyDetailBinding.inflate(inflater)


        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.rvPhoto.adapter = PhotoAdapter(viewModel)

        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvMsg.addItemDecoration(
            DividerItemDecoration(
                appInstance, DividerItemDecoration.VERTICAL
            )
        )
        binding.btnAddPhoto.setOnClickListener {
            pickImage()
        }
        binding.btnMore.setOnClickListener {

            val alert = AlertView("", "", AlertStyle.BOTTOM_SHEET)
            if (viewModel.isJoin.value == true) {
                alert.addAction(AlertAction("我要退出!", AlertActionStyle.NEGATIVE) { _ ->
                    viewModel.leaveParty()
                })
            }



            alert.show(activity as AppCompatActivity)
        }

        binding.tvSeeAll.setOnClickListener {
            viewModel.party.value?.photos?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToAlbumFragment(it.toTypedArray()))

            }
        }

        binding.tvLocation.setOnClickListener {
            findNavController().navigate(PartyDetailFragmentDirections.navToMapFragment(viewModel.party.value?.id))

        }

        viewModel.isSend.observe(viewLifecycleOwner, {
            closeSoftKeyboard(binding.etMsg)
        })

        viewModel.party.observe(viewLifecycleOwner, {
            binding.rvPlayer.adapter = PlayerAdapter(viewModel).apply {
                submitList(it?.playerList)
            }
            viewModel.setGame()
        })

        allGames.observe(viewLifecycleOwner, {
            viewModel.setGame()
        })

        viewModel.partyGames.observe(viewLifecycleOwner, {
            binding.rvPartyGame.adapter = GameAdapter(viewModel).apply {
                submitList(it)
            }
        })

        viewModel.partyMsgs.observe(viewLifecycleOwner, {
            binding.rvMsg.adapter = PartyMsgAdapter(viewModel).apply {
                submitList(it.sortedByDescending { it.createdTime })
            }
        })


        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                if (it.id == "notFound") {

                    val alert = AlertView(
                        getString(R.string.no_game) + it.name,
                        "",
                        AlertStyle.BOTTOM_SHEET
                    )

                    alert.addAction(
                        AlertAction(
                            getString(R.string.add_game_data),
                            AlertActionStyle.POSITIVE
                        ) { _ ->
                            findNavController().navigate(
                                GameFragmentDirections.navToNewGameFragment(
                                    it.name
                                )
                            )
                        })
                    alert.addAction(
                        AlertAction(
                            getString(R.string.cancel),
                            AlertActionStyle.DEFAULT
                        ) { _ ->
                            // Action 2 callback
                        })

                    alert.show(activity as AppCompatActivity)


                } else {
                    findNavController().navigate(
                        PartyDetailFragmentDirections.navToGameDetailFragment(
                            it.id
                        )
                    )
                }
                viewModel.onNavToGameDetail()
            }
        })

        viewModel.navToUser.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToUserDialog(it))
                viewModel.onNavToUser()
            }
        })

        return binding.root
    }

    private fun pickImage() {
        ImagePicker.with(this)              //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startActivityLauncher.launch(intent) { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            data?.let {
                                val fileUri = data.data
                                binding.ivCover.setImageURI(fileUri)
                                viewModel.uploadPhoto(fileUri)
                            }
                        }
                        ImagePicker.RESULT_ERROR -> {
                            ToastUtil.show(ImagePicker.getError(data))
                        }
                    }
                }
            }
    }


}