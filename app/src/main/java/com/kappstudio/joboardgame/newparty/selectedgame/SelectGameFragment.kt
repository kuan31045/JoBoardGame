package com.kappstudio.joboardgame.newparty.selectedgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentSelectGameBinding
import com.kappstudio.joboardgame.newparty.NewPartyViewModel

class SelectGameFragment : Fragment() {
    lateinit var newPartyViewModel: NewPartyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        newPartyViewModel = activity?.run {
            ViewModelProviders.of(this)[NewPartyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSelectGameBinding.inflate(inflater)
        val viewModel: SelectGameViewModel by viewModels()

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        val adapter = SelectGameAdapter(viewModel)
        binding.rvGame.adapter = adapter
        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnAdd.setOnClickListener {
            viewModel.selectedGames.value?.let { it1 -> newPartyViewModel.addGameFromFavorite(it1) }
            findNavController().popBackStack()
        }

        viewModel.games.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })


        viewModel.selectedGames.observe(viewLifecycleOwner, {
            adapter.notifyDataSetChanged()
        })

        return binding.root
    }
}