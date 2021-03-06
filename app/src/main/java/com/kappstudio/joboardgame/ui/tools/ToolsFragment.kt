package com.kappstudio.joboardgame.ui.tools

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentToolsBinding

class ToolsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentToolsBinding.inflate(inflater)

        binding.clDice.setOnClickListener {
            findNavController().navigate(ToolsFragmentDirections.navToDiceFragment())
        }
        binding.clTimer.setOnClickListener {
            findNavController().navigate(ToolsFragmentDirections.navToTimerFragment())
        }
        binding.clSpin.setOnClickListener {
            findNavController().navigate(ToolsFragmentDirections.navToBottleFragment())
        }
        binding.clScoreBoard.setOnClickListener {
            findNavController().navigate(ToolsFragmentDirections.navToScoreboardFragment())
        }
        binding.clDrawLots.setOnClickListener {
            findNavController().navigate(ToolsFragmentDirections.navToDrawLotsFragment())
        }
        binding.clPolygraph.setOnClickListener {
            findNavController().navigate(ToolsFragmentDirections.navToPolygraphFragment())
        }
        return binding.root
    }
}