package com.kappstudio.joboardgame.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentSearchGameBinding
import com.kappstudio.joboardgame.ui.game.AllGameAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SearchGameFragment : Fragment() {

    private val viewModel by lazy {
        requireParentFragment().getViewModel<SearchViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentSearchGameBinding.inflate(inflater)

        viewModel.newGames.observe(viewLifecycleOwner) {
            binding.rvSearchResult.adapter = AllGameAdapter(viewModel).apply {
                submitList(it)
            }
        }

        viewModel.navToGameDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(SearchFragmentDirections.navToGameDetailFragment(it.id))
                viewModel.onNavToGameDetail()
            }
        }

        return binding.root
    }
}