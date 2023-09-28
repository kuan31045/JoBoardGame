package com.kappstudio.joboardgame.ui.partydetail

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.github.dhaval2404.imagepicker.ImagePicker
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.joboardgame.ui.game.GameAdapter
import com.kappstudio.joboardgame.ui.game.GameFragmentDirections
import com.kappstudio.joboardgame.ui.tools.ToolsFragmentDirections
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.util.closeSoftKeyboard
import com.kappstudio.joboardgame.util.ToastUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PartyDetailFragment : Fragment() {

    private lateinit var binding: FragmentPartyDetailBinding
    private lateinit var startActivityLauncher: StartActivityLauncher
    private val viewModel: PartyDetailViewModel by viewModel {
        parametersOf(
            PartyDetailFragmentArgs.fromBundle(
                requireArguments()
            ).clickedPartyId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        startActivityLauncher = StartActivityLauncher(this)

        binding = FragmentPartyDetailBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val gameAdapter = GameAdapter(viewModel)
        val playerAdapter = PlayerAdapter(viewModel)
        val msgAdapter = PartyMsgAdapter(viewModel)

        binding.rvPartyGame.adapter = gameAdapter
        binding.rvPlayer.adapter = playerAdapter
        binding.rvMsg.adapter = msgAdapter
        binding.rvPhoto.adapter = PhotoAdapter()

        binding.rvMsg.addItemDecoration(
            DividerItemDecoration(appInstance, DividerItemDecoration.VERTICAL)
        )

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.tvToDrawLots.setOnClickListener {
            viewModel.party.value?.let {
                findNavController().navigate(
                    ToolsFragmentDirections.navToDrawLotsFragment(it.gameNameList.toTypedArray())
                )
            }
        }

        binding.btnAddPhoto.setOnClickListener {
            pickImage()
        }

        binding.btnMore.setOnClickListener {
            val alert = AlertView(getString(R.string.ask_out), "", AlertStyle.BOTTOM_SHEET)
            if (viewModel.isJoin.value == true) {
                alert.addAction(
                    AlertAction(
                        getString(R.string.i_out),
                        AlertActionStyle.NEGATIVE
                    ) { _ ->
                        viewModel.leaveParty()
                    })
            }
            alert.show(activity as AppCompatActivity)
        }

        binding.tvSeeAll.setOnClickListener {
            viewModel.party.value?.photos?.let {
                findNavController().navigate(
                    PartyDetailFragmentDirections.navToAlbumFragment(it.toTypedArray())
                )
            }
        }

        binding.tvLocation.setOnClickListener {
            findNavController().navigate(
                PartyDetailFragmentDirections.navToMapFragment(viewModel.party.value?.id)
            )
        }

        viewModel.party.observe(viewLifecycleOwner) {
            viewModel.getHostUser()
            viewModel.getGames()
            viewModel.getPlayers()

            viewModel.host.observe(viewLifecycleOwner) {
                binding.tvHost.text = it.name
            }

            viewModel.games.observe(viewLifecycleOwner) {
                gameAdapter.submitList(it)
            }

            viewModel.players.observe(viewLifecycleOwner) {
                playerAdapter.submitList(it)
            }
        }

        viewModel.partyMsgs.observe(viewLifecycleOwner) {
            msgAdapter.submitList(it)
        }

        viewModel.isSend.observe(viewLifecycleOwner) {
            closeSoftKeyboard(binding.etMsg)
        }

        viewModel.hasReported.observe(viewLifecycleOwner) {
            it?.let {
                val mAlert = android.app.AlertDialog.Builder(activity)
                mAlert.setTitle(getString(R.string.report_ok))
                mAlert.setMessage(getString(R.string.google_play_want_see_this3))
                mAlert.setCancelable(true)
                mAlert.setPositiveButton(getString(R.string.ok)) { _, _ ->
                }

                val mAlertDialog = mAlert.create()
                mAlertDialog.show()
                viewModel.hasReported.value = null
            }
        }

        viewModel.navToGameDetail.observe(viewLifecycleOwner) {
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
                                GameFragmentDirections.navToNewGameFragment(it.name)
                            )
                        })

                    alert.addAction(
                        AlertAction(
                            getString(R.string.cancel),
                            AlertActionStyle.DEFAULT
                        ) { _ -> })

                    alert.show(activity as AppCompatActivity)
                } else {
                    findNavController().navigate(
                        PartyDetailFragmentDirections.navToGameDetailFragment(it.id)
                    )
                }
                viewModel.onNavToGameDetail()
            }
        }

        viewModel.navToUser.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToUserFragment(it))
                viewModel.onNavToUser()
            }
        }

        return binding.root
    }

    private fun pickImage() {
        ImagePicker.with(this)         //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1080
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                viewModel.status.value = LoadApiStatus.LOADING

                startActivityLauncher.launch(intent) { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            data?.let {
                                val fileUri = data.data
                                if (fileUri != null) {
                                    viewModel.uploadPhoto(fileUri)
                                }
                            }
                        }

                        Activity.RESULT_CANCELED -> {
                            viewModel.status.value = LoadApiStatus.DONE
                        }

                        ImagePicker.RESULT_ERROR -> {
                            ToastUtil.show(ImagePicker.getError(data))
                            viewModel.status.value = LoadApiStatus.DONE
                        }
                    }
                }
            }
    }
}