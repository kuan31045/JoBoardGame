package com.kappstudio.joboardgame.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentSearchPartyBinding
import com.kappstudio.joboardgame.databinding.FragmentSearchUserBinding
import com.kappstudio.joboardgame.partydetail.PartyDetailFragmentDirections


class SearchUserFragment : Fragment() {

    lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireParentFragment()).get(SearchViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchUserBinding.inflate(inflater)
        viewModel.newUsers.observe(viewLifecycleOwner,{
                binding.rvSearchResult.adapter = UserAdapter(viewModel).apply {  submitList(
                    it
                )}
        })
        viewModel.navToUser.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(PartyDetailFragmentDirections.navToUserFragment(it))
                viewModel.onNavToUser()
            }
        })
        return binding.root
    }


}