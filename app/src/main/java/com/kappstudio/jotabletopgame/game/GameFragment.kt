package com.kappstudio.jotabletopgame.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kappstudio.jotabletopgame.databinding.FragmentGameBinding

class GameFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentGameBinding.inflate(inflater)

        // set View Pager Adapter
        val sectionsPagerAdapter = GamePagerAdapter(childFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter

        // set Tabs Layout Adapter
        binding.tabsLayout.setupWithViewPager(binding.viewPager)

        return binding.root
    }
}
