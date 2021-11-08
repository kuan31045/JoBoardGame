package com.kappstudio.joboardgame.album

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.databinding.FragmentPhotoItemBinding


class PhotoItemFragment(private val photo: String) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPhotoItemBinding.inflate(inflater)

        bindImage(binding.zoomageView,photo)

        return binding.root
    }


}