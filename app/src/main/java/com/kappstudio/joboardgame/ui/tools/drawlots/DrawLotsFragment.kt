package com.kappstudio.joboardgame.ui.tools.drawlots

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentDrawLotsBinding
import tech.gujin.toast.ToastUtil

class DrawLotsFragment : Fragment() {
    lateinit var binding: FragmentDrawLotsBinding
    lateinit var viewModel: DrawLotsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this)[DrawLotsViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        binding = FragmentDrawLotsBinding.inflate(inflater)

        DrawLotsFragmentArgs.fromBundle(requireArguments()).gameList?.let {
            viewModel.setItems(it.toList())
        }

        binding.etAddItem.setOnFocusChangeListener { v, hasFocus ->
            binding.etAddItem.let { ContextCompat.getSystemService(it.context, InputMethodManager::class.java) }
                ?.showSoftInput(binding.etAddItem, InputMethodManager.SHOW_IMPLICIT)
        }

        binding.btnAdd.setOnClickListener {
            addItem()
        }
        binding.etAddItem.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                addItem()
                return@OnEditorActionListener true
            }
            false
        })

        binding.btnDraw.setOnClickListener {
            if (viewModel.items.value?.isEmpty() == true) {
                ToastUtil.show(getString(R.string.plz_input_draw_item))
                binding.etAddItem.requestFocus()
            } else {
                viewModel.draw()
            }
        }

        viewModel.items.observe(viewLifecycleOwner, {
            binding.rvItem.adapter = DrawItemAdapter(viewModel).apply {
                submitList(it)
            }
        })

        viewModel.navToDraw.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(DrawLotsFragmentDirections.navToDrawDialog(it))
                viewModel.onNavToDraw()
            }
        })

        return binding.root
    }

    private fun addItem() {
        if (binding.etAddItem.text.replace("\\s".toRegex(), "").isEmpty()) {
            ToastUtil.show(getString(R.string.cant_empty))
        } else {
            viewModel.addItem(binding.etAddItem.text.toString())
            binding.etAddItem.setText("")
        }
    }




}