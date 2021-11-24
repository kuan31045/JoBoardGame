package com.kappstudio.joboardgame.tools.polygraph

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kappstudio.joboardgame.databinding.FragmentPolygraphBinding

class PolygraphFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPolygraphBinding.inflate(inflater)
        val viewModel: PolygraphViewModel by viewModels()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        viewModel.isTesting.observe(viewLifecycleOwner,{













            if (it){


                





















            }














            else{

            }
        })



        return binding.root
    }
}