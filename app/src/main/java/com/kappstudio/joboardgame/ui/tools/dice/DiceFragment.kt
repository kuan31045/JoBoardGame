package com.kappstudio.joboardgame.ui.tools.dice

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kappstudio.joboardgame.databinding.FragmentDiceBinding

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

        viewModel.qty.observe(viewLifecycleOwner, {
            it?.let {
                val amount = mutableListOf<Int>()
                repeat(it) { i ->
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