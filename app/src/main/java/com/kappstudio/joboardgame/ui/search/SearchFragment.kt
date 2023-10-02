package com.kappstudio.joboardgame.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.databinding.FragmentSearchBinding
import com.kappstudio.joboardgame.util.closeSoftKeyboard
import com.kappstudio.joboardgame.util.openKeyBoard
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentSearchBinding.inflate(inflater)

        // Setup View Pager Adapter
        val adapter = SearchPagerAdapter(childFragmentManager)

        binding.viewPager.adapter = adapter
        binding.viewPager.currentItem = SearchFragmentArgs.fromBundle(requireArguments()).position

        // Setup Tabs Layout Adapter
        binding.tabsLayout.setupWithViewPager(binding.viewPager)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.ivClear.setOnClickListener {
            binding.etSearch.setText("")
        }

        binding.etSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                closeSoftKeyboard(binding.etSearch)
                return@OnEditorActionListener true
            }
            false
        })

        viewModel.isInit.observe(viewLifecycleOwner) {
            if (it) {
                openKeyBoard(binding.etSearch)
                viewModel.onInit()
            }
        }

        return binding.root
    }
}