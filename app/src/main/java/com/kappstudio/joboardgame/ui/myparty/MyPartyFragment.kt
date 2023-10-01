package com.kappstudio.joboardgame.ui.myparty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.bindNotFoundLottie
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.databinding.FragmentMyPartyBinding
import com.kappstudio.joboardgame.ui.party.PartyAdapter
import com.kappstudio.joboardgame.ui.party.PartyFragmentDirections
import com.kappstudio.joboardgame.util.ToastUtil
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MyPartyFragment : Fragment() {

    private val viewModel: MyPartyViewModel by viewModel {
        parametersOf(
            MyPartyFragmentArgs.fromBundle(requireArguments()).userId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentMyPartyBinding.inflate(inflater)
        val adapter = PartyAdapter(viewModel)

        binding.rvParty.adapter = adapter

        viewModel.parties.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    val parties = result.data
                    adapter.submitList(result.data)
                    bindNotFoundLottie(binding.lottieNotFound, binding.tvNotFound, parties)
                    binding.lottieLoading.visibility = View.GONE
                }

                is Result.Loading -> binding.lottieLoading.visibility = View.VISIBLE

                else -> {
                    ToastUtil.show(getString(R.string.check_internet))
                    binding.lottieLoading.visibility = View.GONE
                }
            }
        }

        viewModel.navToPartyDetail.observe(viewLifecycleOwner) {
            it?.let {
                findNavController().navigate(PartyFragmentDirections.navToPartyDetailFragment(it))
                viewModel.onNavToPartyDetail()
            }
        }

        return binding.root
    }
}