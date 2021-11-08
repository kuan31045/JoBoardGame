package com.kappstudio.joboardgame.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.databinding.FragmentPhotoBinding


class PhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPhotoBinding.inflate(inflater)

        // set View Pager Adapter
        val adapter = PhotoPagerAdapter(
            childFragmentManager,
            PhotoFragmentArgs.fromBundle(requireArguments()).photos
            )

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = PhotoFragmentArgs.fromBundle(requireArguments()).position

        return binding.root
    }
}