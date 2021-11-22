package com.kappstudio.joboardgame.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentNotificationBinding
import com.kappstudio.joboardgame.databinding.FragmentProfileBinding

class NotificationFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNotificationBinding.inflate(inflater)


        return binding.root
    }

}