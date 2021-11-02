package com.kappstudio.jotabletopgame.newparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.kappstudio.jotabletopgame.R

import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.databinding.FragmentNewPartyBinding
import com.kappstudio.jotabletopgame.favorite.FavoriteFragmentDirections
import timber.log.Timber

class NewPartyFragment : Fragment() {

    lateinit var binding: FragmentNewPartyBinding
    val viewModel: NewPartyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPartyBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.country.observe(viewLifecycleOwner, {
            Timber.d("country $it")
        })
        viewModel.navToGameDetail.observe(viewLifecycleOwner, {
            it?.let {
                findNavController().navigate(FavoriteFragmentDirections.navToGameDetailFragment(it))
                viewModel.onNavToGameDetail()
            }
        })
        viewModel.games.observe(viewLifecycleOwner,{
            Timber.d("games: $it")
            binding.rvGame.adapter = AddGameAdapter(viewModel).apply {
                submitList(it.reversed())
            }
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
            .mainColor(appInstance.getColor(R.color.blue_8187ff))
            .titleTextColor(appInstance.getColor(R.color.blue_8187ff))
            .displayListener {}

            .title(getString(R.string.data_time))
            .listener { date ->
                binding.etTime.setText(date.toString())
                viewModel.time.value = date.time
            }
            .display()
    }

}