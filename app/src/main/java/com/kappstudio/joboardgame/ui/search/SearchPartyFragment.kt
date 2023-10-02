package com.kappstudio.joboardgame.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentSearchPartyBinding
import com.kappstudio.joboardgame.ui.party.PartyAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

class SearchPartyFragment : Fragment() {

    private val viewModel by lazy {
        requireParentFragment().getViewModel<SearchViewModel>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentSearchPartyBinding.inflate(inflater)

        viewModel.parties.observe(viewLifecycleOwner) {}

        viewModel.newParties.observe(viewLifecycleOwner) {
            binding.rvSearchResult.adapter = PartyAdapter(viewModel).apply { submitList(it) }
        }

        viewModel.navToPartyDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(SearchFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        }

        return binding.root
    }
}