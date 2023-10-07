package com.kappstudio.joboardgame.ui.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentAlbumBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class AlbumFragment : Fragment() {

    private val viewModel: AlbumViewModel by viewModel {
        parametersOf(
            AlbumFragmentArgs.fromBundle(
                requireArguments()
            ).photos.toList()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentAlbumBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.rvPhoto.adapter = AlbumAdapter(viewModel)

        viewModel.navToPhoto.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(
                    AlbumFragmentDirections.navToPhotoFragment(
                        it.reversed()
                            .toTypedArray(), viewModel.position.value ?: 0
                    )
                )
                viewModel.onNavToPhoto()
            }
        }

        return binding.root
    }
}