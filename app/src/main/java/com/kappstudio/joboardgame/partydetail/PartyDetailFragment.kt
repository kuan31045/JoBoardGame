package com.kappstudio.joboardgame.partydetail

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.github.dhaval2404.imagepicker.ImagePicker

import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.joboardgame.game.GameAdapter
import com.kappstudio.joboardgame.party.PhotoAdapter
import tech.gujin.toast.ToastUtil

class PartyDetailFragment : Fragment() {
    lateinit var binding : FragmentPartyDetailBinding
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
        viewModel.party.observe(viewLifecycleOwner, {
            binding.rvPlayer.adapter = PlayerAdapter(viewModel).apply {
                submitList(it?.playerList)
            }
            binding.rvPartyGame.adapter = GameAdapter(viewModel).apply {
                submitList(it?.gameList)
            }

        })

        viewModel.partyMsgs.observe(viewLifecycleOwner, {
            binding.rvMsg.adapter = PartyMsgAdapter(viewModel).apply {
                submitList(it.sortedByDescending { it.createdTime })
            }
        })


        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(
                    PartyDetailFragmentDirections.navToGameDetailFragment(
                        it
                    )
                )
                viewModel.onNavToGameDetail()
            }
        })

        viewModel.navToUser.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToUserDialog(it))
                viewModel.onNavToUser()
            }
        })

        viewModel.navToAlbum.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToAlbumFragment(it.toTypedArray()))
                viewModel.onNavToAlbum()
            }
        })
        viewModel.navToMap.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToMapFragment(it))
                viewModel.onNavToMap()
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