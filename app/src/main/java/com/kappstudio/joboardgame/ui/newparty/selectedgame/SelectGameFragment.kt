package com.kappstudio.joboardgame.ui.newparty.selectedgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentSelectGameBinding
import com.kappstudio.joboardgame.ui.newparty.NewPartyViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SelectGameFragment : Fragment() {

    private val selectGameViewModel: SelectGameViewModel by viewModel()
    private val newPartyViewModel by activityViewModel<NewPartyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentSelectGameBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = selectGameViewModel

        val adapter = SelectGameAdapter(selectGameViewModel)
        binding.rvGame.adapter = adapter

        binding.ivClose.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnAdd.setOnClickListener {
            selectGameViewModel.selectedGames.value?.let { games ->
                newPartyViewModel.addGameFromFavorite(games)
            }
            findNavController().popBackStack()
        }

        selectGameViewModel.games.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        selectGameViewModel.selectedGames.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        return binding.root
    }
}