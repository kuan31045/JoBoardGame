package com.kappstudio.joboardgame.ui.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.factory.VMFactory
import com.kappstudio.joboardgame.databinding.FragmentAlbumBinding

class AlbumFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAlbumBinding.inflate(inflater)

        val viewModel: AlbumViewModel by viewModels {
            VMFactory {
                AlbumViewModel(
                    AlbumFragmentArgs.fromBundle(requireArguments()).photos.toList(),
                )
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.rvPhoto.adapter = AlbumAdapter(viewModel)

        viewModel.navToPhoto.observe(viewLifecycleOwner,{
            it?.let {
                findNavController().navigate(AlbumFragmentDirections.navToPhotoFragment(it.sortedByDescending { it }.toTypedArray(),viewModel.position.value?:0))
                viewModel.onNavToPhoto()
            }
        })

        return binding.root
    }


}