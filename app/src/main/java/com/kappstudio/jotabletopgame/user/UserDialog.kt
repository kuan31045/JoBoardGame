package com.kappstudio.jotabletopgame.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.bindImage
import com.kappstudio.jotabletopgame.databinding.DialogUserBinding
import com.kappstudio.jotabletopgame.partydetail.PartyDetailFragmentArgs
import com.kappstudio.jotabletopgame.partydetail.PartyDetailViewModel

class UserDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogUserBinding.inflate(inflater, container, false)
        val viewModel: UserViewModel by viewModels {
            VMFactory {
                UserViewModel(
                    UserDialogArgs.fromBundle(requireArguments()).clickedUserId,
                )
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.ivClose.setOnClickListener { dismiss() }



        return binding.root
    }
}