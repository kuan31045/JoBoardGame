package com.kappstudio.jotabletopgame.newparty

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.kappstudio.jotabletopgame.R

import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.bindSpinnerCountries
import com.kappstudio.jotabletopgame.data.User
import com.kappstudio.jotabletopgame.databinding.FragmentNewPartyBinding
import com.kappstudio.jotabletopgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.jotabletopgame.game.GameAdapter
import com.kappstudio.jotabletopgame.game.GameAdapterType
import com.kappstudio.jotabletopgame.home.HomeViewModel
import tech.gujin.toast.ToastUtil
import timber.log.Timber
import java.util.ArrayList
import kotlin.time.milliseconds

class NewPartyFragment : Fragment() {

    lateinit var binding: FragmentNewPartyBinding
    val viewModel: NewPartyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNewPartyBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.country.observe(viewLifecycleOwner, {
            Timber.d("country $it")
        })

        binding.etTime.setOnClickListener {
            showTimePicker()
        }
        return binding.root
    }

    private fun showTimePicker() {
        SingleDateAndTimePickerDialog.Builder(context)
            .bottomSheet()
            .curved()
            .backgroundColor(appInstance.getColor(R.color.white))
            .mainColor(R.color.white)
            .titleTextColor(R.color.white)
            .displayListener {}

            .title(getString(R.string.data_time))
            .listener { date ->
                binding.etTime.setText(date.toString())
                viewModel.time.value = date.time
            }
            .display()
    }

}