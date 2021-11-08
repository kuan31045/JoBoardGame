package com.kappstudio.joboardgame.newparty

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentNewPartyBinding
import com.kappstudio.joboardgame.favorite.FavoriteFragmentDirections
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.kappstudio.joboardgame.R

import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.source.remote.LoadApiStatus
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class NewPartyFragment : Fragment() {

    lateinit var binding: FragmentNewPartyBinding
    val viewModel: NewPartyViewModel by viewModels()

    lateinit var startActivityLauncher: StartActivityLauncher


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPartyBinding.inflate(inflater)
        startActivityLauncher = StartActivityLauncher(this)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnAddCover.setOnClickListener {
            pickImage()
        }
        binding.etTime.setOnClickListener {
            showTimePicker()
        }
        binding.etLocation.setOnClickListener {
            startAutoCompleteIntent()
        }

        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(FavoriteFragmentDirections.navToGameDetailFragment(it))
                viewModel.onNavToGameDetail()
            }
        })
        viewModel.games.observe(viewLifecycleOwner, {
            Timber.d("games: $it")
            binding.rvGame.adapter = AddGameAdapter(viewModel).apply {
                submitList(it.reversed())
            }
        })

        viewModel.status.observe(viewLifecycleOwner,{
            when(it){
                LoadApiStatus.DONE -> findNavController().popBackStack()

            }
        })

        return binding.root
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop(
                2f,
                1f
            )                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
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


    private fun showTimePicker() {
        SingleDateAndTimePickerDialog.Builder(context)
            .bottomSheet()
            .curved()
            .backgroundColor(appInstance.getColor(R.color.white))
            .mainColor(appInstance.getColor(R.color.blue_8187ff))
            .titleTextColor(appInstance.getColor(R.color.blue_8187ff))
            .displayListener {}

            .title(getString(R.string.data_time))
            .listener { date ->
                binding.etTime.setText(date.toString())
                viewModel.time.value = date.time
            }
            .display()
    }

}