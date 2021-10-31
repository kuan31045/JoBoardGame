package com.kappstudio.jotabletopgame.gamedetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kappstudio.jotabletopgame.*
import com.kappstudio.jotabletopgame.databinding.FragmentGameDetailBinding
import timber.log.Timber


class GameDetailFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameDetailBinding.inflate(inflater)
        val viewModel: GameDetailViewModel by viewModels {
            VMFactory {
                GameDetailViewModel(
                    GameDetailFragmentArgs.fromBundle(requireArguments()).clickedGameId,
                    appInstance.provideJoRepository()
                )
            }
        }
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.game.observe(viewLifecycleOwner, {
            it?.let{
                Timber.d("game= $it")
                bindImage(binding.ivGame, it.image)
                bindTextViewGameTypes(binding.tvType,it.type)
                binding.nsvMain.post{
                    binding.nsvMain.fullScroll(View.FOCUS_UP);

                    binding.nsvMain.scrollTo(0, 200)   }
                viewModel.addViewedGame() //加入瀏覽紀錄
                viewModel.checkFavorite()
            }
        })



        return binding.root
    }


}