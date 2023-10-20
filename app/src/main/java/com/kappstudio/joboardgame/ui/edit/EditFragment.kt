package com.kappstudio.joboardgame.ui.edit

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.github.dhaval2404.imagepicker.ImagePicker
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentEditBinding
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.util.ToastUtil
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private val viewModel: EditViewModel by viewModel()
    private lateinit var startActivityLauncher: StartActivityLauncher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        startActivityLauncher = StartActivityLauncher(this)
        binding = FragmentEditBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.btnEditImage.setOnClickListener {
            pickImage()
        }

        binding.tvUgc.setOnClickListener {
            val mAlert = android.app.AlertDialog.Builder(activity)
            mAlert.setTitle(getString(R.string.see_rule))
            mAlert.setMessage(getString(R.string.rule))
            mAlert.setCancelable(false)
            mAlert.setPositiveButton(getString(R.string.ok)) { _, _ ->
            }
            val mAlertDialog = mAlert.create()
            mAlertDialog.show()
        }

        viewModel.status.observe(viewLifecycleOwner) {
            when (it) {
                LoadApiStatus.DONE -> findNavController().popBackStack()
                LoadApiStatus.ERROR -> ToastUtil.show(getString(R.string.create_failed))
                else -> {}
            }
        }

        return binding.root
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop(
                1f,
                1f
            )                        //Crop image(Optional), Check Customization for more option
            .compress(1024)                   //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                540,
                540
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startActivityLauncher.launch(intent) { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            data?.let {
                                val fileUri = data.data
                                binding.ivProfile.setImageURI(fileUri)
                                viewModel.imageUri.value = fileUri
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