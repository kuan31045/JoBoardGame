package com.kappstudio.joboardgame.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import timber.log.Timber
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentSearchBinding
import com.kappstudio.joboardgame.util.closeSoftKeyboard
import com.kappstudio.joboardgame.util.openKeyBoard
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener

class SearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentSearchBinding.inflate(inflater)
        val viewModel: SearchViewModel by viewModels()
        Timber.d("${viewModel.search.value}")
        // set View Pager Adapter
        val adapter = SearchPagerAdapter(childFragmentManager)

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = SearchFragmentArgs.fromBundle(requireArguments()).position

        // set Tabs Layout Adapter
        binding.tabsLayout.setupWithViewPager(binding.viewPager)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        if (viewModel.isInit) {
            openKeyBoard(binding.etSearch)
            viewModel.onInit()
        }

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivClear.setOnClickListener {
            binding.etSearch.setText("")
        }

        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                 closeSoftKeyboard(binding.etSearch)
                return@OnEditorActionListener true
            }
            false
        })
        return binding.root
    }
}