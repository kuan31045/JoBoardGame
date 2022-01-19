package com.kappstudio.joboardgame.ui.partydetail

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
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
import com.kappstudio.joboardgame.*
import com.kappstudio.joboardgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.joboardgame.factory.VMFactory
import com.kappstudio.joboardgame.ui.game.GameAdapter
import com.kappstudio.joboardgame.ui.game.GameFragmentDirections
import com.kappstudio.joboardgame.ui.party.PhotoAdapter
import com.kappstudio.joboardgame.ui.tools.ToolsFragmentDirections
import com.kappstudio.joboardgame.util.closeSoftKeyboard
import tech.gujin.toast.ToastUtil

class PartyDetailFragment : Fragment() {

    private lateinit var binding: FragmentPartyDetailBinding
    private lateinit var startActivityLauncher: StartActivityLauncher
    val viewModel: PartyDetailViewModel by viewModels {
        VMFactory {
            PartyDetailViewModel(
                PartyDetailFragmentArgs.fromBundle(requireArguments()).clickedPartyId,
                appInstance.provideJoRepository()
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

        val gameAdapter = GameAdapter(viewModel)
        val playerAdapter = PlayerAdapter(viewModel)
        val msgAdapter = PartyMsgAdapter(viewModel)

        binding.rvPartyGame.adapter = gameAdapter
        binding.rvPlayer.adapter = playerAdapter
        binding.rvMsg.adapter = msgAdapter
        binding.rvPhoto.adapter = PhotoAdapter(viewModel)

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

        viewModel.isSend.observe(viewLifecycleOwner, {
            closeSoftKeyboard(binding.etMsg)
        })

        viewModel.party.observe(viewLifecycleOwner, {
            viewModel.getHostUser()
            viewModel.getGames()
            viewModel.getPlayers()

            viewModel.host.observe(viewLifecycleOwner, {
                binding.tvHost.text = it.name
            })

            viewModel.games.observe(viewLifecycleOwner, {
                gameAdapter.submitList(it)
            })

            viewModel.players.observe(viewLifecycleOwner, {
                playerAdapter.submitList(it)
            })
        })

        viewModel.partyMsgs.observe(viewLifecycleOwner, {
            msgAdapter.submitList(it)
        })

        viewModel.reportOk.observe(viewLifecycleOwner, {
            it?.let {
                ToastUtil.show(getString(R.string.report_ok))
                val builder = context?.let { it1 -> AlertDialog.Builder(it1) }
                builder?.setMessage(getString(R.string.google_play_want_see_this3))
                builder?.show()

                viewModel.onReportOk()
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
                                GameFragmentDirections.navToNewGameFragment(it.name)
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
                        PartyDetailFragmentDirections.navToGameDetailFragment(it.id)
                    )
                }
                viewModel.onNavToGameDetail()
            }
        })

        viewModel.navToUser.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToUserFragment(it))
                viewModel.onNavToUser()
            }
        })

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