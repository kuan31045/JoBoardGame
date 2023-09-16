package com.kappstudio.joboardgame.ui.party

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.FragmentPartyBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.util.ToastUtil

class PartyFragment : Fragment() {

    private val viewModel: PartyViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = FragmentPartyBinding.inflate(inflater)
        val adapter = PartyAdapter(viewModel)

        binding.rvParty.adapter = adapter

        binding.btnNewParty.setOnClickListener {
            findNavController().navigate(PartyFragmentDirections.navToNewPartyFragment())
        }

        viewModel.parties.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Success -> {
                    adapter.submitList(result.data)
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