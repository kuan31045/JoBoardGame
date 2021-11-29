package com.kappstudio.joboardgame.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentSearchPartyBinding
import com.kappstudio.joboardgame.party.PartyAdapter

class SearchPartyFragment : Fragment() {

    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment()).get(SearchViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchPartyBinding.inflate(inflater)

//        viewModel.newParties.observe(viewLifecycleOwner,{
//            binding.rvSearchResult.adapter = PartyAdapter(
//                viewModel,
//                appInstance.provideJoRepository()
//            ).apply {  submitList(
//                it
//            )}
//        })
        viewModel.navToPartyDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(SearchFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        })

        return binding.root
    }


}