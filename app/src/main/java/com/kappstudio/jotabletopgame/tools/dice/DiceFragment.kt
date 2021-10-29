package com.kappstudio.jotabletopgame.tools.dice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kappstudio.jotabletopgame.databinding.FragmentDiceBinding

class DiceFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDiceBinding.inflate(inflater)
        val viewModel: DiceViewModel by viewModels()
        binding.lifecycleOwner = viewLifecycleOwner

        binding.viewModel = viewModel
        val adapter = DiceAdapter(viewModel)
        binding.rvDice.adapter = adapter

        viewModel.qty.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                val amount = mutableListOf<Int>()
                for (i in 1..it) {
                    amount.add(i)
                }
                adapter.submitList(amount)
            }
        })

        viewModel.isRolling.observe(viewLifecycleOwner, {
            if (it) {
                adapter.notifyDataSetChanged()
            }
        })

        return binding.root
    }
}