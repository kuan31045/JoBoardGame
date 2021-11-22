package com.kappstudio.joboardgame.album

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.joboardgame.bindImage
import com.kappstudio.joboardgame.databinding.FragmentPhotoItemBinding
import android.view.MotionEvent
import java.lang.IllegalArgumentException


class PhotoItemFragment(private val photo: String) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPhotoItemBinding.inflate(inflater)

        bindImage(binding.photoView,photo)

        return binding.root
    }


}