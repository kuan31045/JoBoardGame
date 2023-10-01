package com.kappstudio.joboardgame.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.DialogRatingBinding
import com.kappstudio.joboardgame.screenHeight
import com.kappstudio.joboardgame.util.setBlurView
import android.widget.RatingBar.OnRatingBarChangeListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RatingDialog : BottomSheetDialogFragment() {

    private val viewModel: RatingViewModel by viewModel {
        parametersOf(
            RatingDialogArgs.fromBundle(
                requireArguments()
            ).rating
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogBg)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val binding = DialogRatingBinding.inflate(inflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        activity?.let { setBlurView(it, binding.blurView) }

        binding.ivClose.setOnClickListener { dismiss() }

        binding.ratingBar.onRatingBarChangeListener = OnRatingBarChangeListener { _, rating, _ ->
            viewModel.score.value = rating.toInt()
        }

        viewModel.dismiss.observe(viewLifecycleOwner) {
            it?.let { dismiss() }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val view: FrameLayout = dialog?.findViewById(R.id.design_bottom_sheet)!!
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        val behavior = BottomSheetBehavior.from(view)
        behavior.peekHeight = screenHeight
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
}