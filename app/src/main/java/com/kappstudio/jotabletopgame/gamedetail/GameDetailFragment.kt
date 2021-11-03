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
import com.kappstudio.jotabletopgame.myparty.MyPartyFragmentDirections
import com.kappstudio.jotabletopgame.util.setBlurView
import tech.gujin.toast.ToastUtil
import timber.log.Timber


class GameDetailFragment : Fragment() {
    val viewModel: GameDetailViewModel by viewModels {
        VMFactory {
            GameDetailViewModel(
                GameDetailFragmentArgs.fromBundle(requireArguments()).clickedGameId,
                appInstance.provideJoRepository()
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameDetailBinding.inflate(inflater)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        binding.ivBack.setOnClickListener { findNavController().popBackStack() }

        viewModel.game.observe(viewLifecycleOwner, {
            it?.let {
                Timber.d("game= $it")
                bindImage(binding.ivGame, it.image)
                bindTextViewGameTypes(binding.tvType, it.type)

                viewModel.addViewedGame() //加入瀏覽紀錄
                viewModel.checkFavorite()
                viewModel.checkRating()
                viewModel.calAvgRating()

            }
        })



        viewModel.navToRating.observe(viewLifecycleOwner, {
            it?.let { it ->
                findNavController().navigate(GameDetailFragmentDirections.navToRatingDialog(it))
                viewModel.onNavToRating()
            }
        })



        return binding.root
    }

    override fun onResume() {

        super.onResume()
        Timber.d("onResume")
     }


}