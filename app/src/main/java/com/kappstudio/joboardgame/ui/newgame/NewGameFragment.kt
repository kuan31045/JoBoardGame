package com.kappstudio.joboardgame.ui.newgame

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.github.dhaval2404.imagepicker.ImagePicker
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.databinding.FragmentNewGameBinding 
import com.kappstudio.joboardgame.util.ToastUtil

class NewGameFragment : Fragment() {
    lateinit var binding: FragmentNewGameBinding
    private lateinit var startActivityLauncher: StartActivityLauncher
    val viewModel: NewGameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewGameBinding.inflate(inflater)
        startActivityLauncher = StartActivityLauncher(this)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.name.value=NewGameFragmentArgs.fromBundle(requireArguments()).gameName


        val typeList = resources.getStringArray(R.array.game_type_list).toList()
        val toolList = resources.getStringArray(R.array.tool_list).toList()

        binding.rvType.adapter = TypeAdapter(viewModel).apply {
            submitList(typeList)
        }

        binding.rvTool.adapter = ToolAdapter(viewModel).apply {
            submitList(toolList)
        }

        binding.btnAddImage.setOnClickListener {
            pickImage()
        }

        viewModel.invalidPublish.observe(viewLifecycleOwner) {
            it?.let {
                ToastUtil.show(it.msg)
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
                1f,
                1.1f
            )                    //Crop image(Optional), Check Customization for more option
            .compress(1024)            //Final image size will be less than 1 MB(Optional)
            .maxResultSize(
                1080,
                1188
            )    //Final image resolution will be less than 1080 x 1080(Optional)
            .createIntent { intent ->
                startActivityLauncher.launch(intent) { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            data?.let {
                                val fileUri = data.data
                                binding.ivCover.setImageURI(fileUri)
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