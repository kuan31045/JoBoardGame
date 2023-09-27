package com.kappstudio.joboardgame.ui.album.photo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.joboardgame.databinding.FragmentPhotoBinding


class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentPhotoBinding.inflate(inflater)

        // set View Pager Adapter
        val photos = PhotoFragmentArgs.fromBundle(requireArguments()).photos
        val adapter = PhotoPagerAdapter(
            childFragmentManager,
            photos
        )

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = PhotoFragmentArgs.fromBundle(requireArguments()).position

        return binding.root
    }
}