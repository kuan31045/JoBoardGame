package com.kappstudio.joboardgame.ui.newparty

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.kappstudio.joboardgame.databinding.FragmentNewPartyBinding
import com.kappstudio.joboardgame.ui.favorite.FavoriteFragmentDirections
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.allGames
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import com.kappstudio.joboardgame.ui.game.GameFragmentDirections
import com.kappstudio.joboardgame.ui.game.GameViewModel
import com.kappstudio.joboardgame.util.closeSoftKeyboard
import com.kappstudio.joboardgame.util.ToastUtil
import timber.log.Timber


class NewPartyFragment : Fragment() {
    lateinit var binding: FragmentNewPartyBinding
    lateinit var viewModel: NewPartyViewModel

    private lateinit var startActivityLauncher: StartActivityLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this)[NewPartyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentNewPartyBinding.inflate(inflater)
        val gameViewModel: GameViewModel by viewModels()
        startActivityLauncher = StartActivityLauncher(this)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnAddCover.setOnClickListener {
            pickImage()
        }

        binding.tvAddGame.setOnClickListener {
            findNavController().navigate(NewPartyFragmentDirections.navToSelectGameFragment())
        }

        binding.actAddGame.setOnItemClickListener { _, _, _, _ ->
            viewModel.addGame()
        }

        gameViewModel.games.observe(viewLifecycleOwner) {
            var query = arrayListOf<String>()
            for (game in it) {
                query.add(game.name)
            }
            val arraryAdapter = context?.let { it1 ->
                ArrayAdapter(
                    it1,
                    android.R.layout.simple_list_item_1,
                    query
                )
            }
            binding.actAddGame.setAdapter(arraryAdapter)
        }

        var isPickingTime = false

        val builder = SingleDateAndTimePickerDialog.Builder(context)
            .bottomSheet()
            .curved()
            .backgroundColor(appInstance.getColor(R.color.white))
            .mainColor(appInstance.getColor(R.color.blue_8187ff))
            .titleTextColor(appInstance.getColor(R.color.blue_8187ff))
            .displayListener { }
            .displayListener {
                isPickingTime = true
            }
            .title(getString(R.string.data_time))
            .listener { date ->
                binding.etTime.setText(date.toString())
                viewModel.partyTime.value = date.time
                isPickingTime = false

            }

        binding.rvGame.addItemDecoration(
            DividerItemDecoration(
                appInstance, DividerItemDecoration.VERTICAL
            )
        )

        binding.etTime.setOnClickListener {
            closeSoftKeyboard(binding.etTitle)
            builder.display()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (isPickingTime) {
                        builder.close()
                        isPickingTime = false
                    } else {
                        findNavController().popBackStack()
                    }
                }
            })

        binding.etLocation.setOnClickListener {
            startAutoCompleteIntent()
        }

        viewModel.invalidPublish.observe(viewLifecycleOwner) {
            it?.let {
                ToastUtil.show(it.msg)
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
                        FavoriteFragmentDirections.navToGameDetailFragment(it.id)
                    )
                }

                viewModel.onNavToGameDetail()
            }
        }

        viewModel.gameNameList.observe(viewLifecycleOwner) {
            viewModel.setGame()
        }

        allGames.observe(viewLifecycleOwner) {
            viewModel.setGame()
        }

        viewModel.partyGames.observe(viewLifecycleOwner) {
            binding.rvGame.adapter = AddGameAdapter(viewModel).apply {
                submitList(it)
            }
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                LoadApiStatus.DONE -> findNavController().popBackStack()
                else -> {}
            }
        }

        return binding.root
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop(
                2f,
                1f
            )                        //Crop image(Optional), Check Customization for more option
            .compress(1024)                   //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                540
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startActivityLauncher.launch(intent) { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            data?.let {
                                val fileUri = data.data
                                binding.ivCover.setImageURI(fileUri)
                                viewModel.photoUri.value = fileUri
                            }
                        }

                        ImagePicker.RESULT_ERROR -> {
                            ToastUtil.show(ImagePicker.getError(data))
                        }
                    }
                }
            }
    }

    private fun startAutoCompleteIntent() {

        // Set the fields to specify which types of place data to
        // return after the user has made a selection.
        val fields = listOf(Place.Field.ADDRESS, Place.Field.LAT_LNG)

        // Start the autocomplete intent.
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .setTypeFilter(TypeFilter.ADDRESS)
            .build(activity)

        startActivityLauncher.launch(intent) { resultCode, data ->
            when (resultCode) {
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(it)
                        binding.etLocation.setText(place.address)
                        viewModel.lat.value = place.latLng.latitude
                        viewModel.lng.value = place.latLng.longitude

                        Timber.d("Place: ${place.name}, ${place.id}")
                    }
                }

                AutocompleteActivity.RESULT_ERROR -> {
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Timber.d(status.statusMessage)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshGame()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity?.viewModelStore?.clear()
    }
}