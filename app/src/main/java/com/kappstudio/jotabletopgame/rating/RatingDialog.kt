package com.kappstudio.jotabletopgame.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.VMFactory
import com.kappstudio.jotabletopgame.databinding.DialogRatingBinding
import com.kappstudio.jotabletopgame.screenHeight
import com.kappstudio.jotabletopgame.util.setBlurView
import android.widget.RatingBar

import android.widget.RatingBar.OnRatingBarChangeListener
import tech.gujin.toast.ToastUtil


class RatingDialog : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogBg)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DialogRatingBinding.inflate(inflater)
        val viewModel: RatingViewModel by viewModels {
            VMFactory {
                RatingViewModel(
                    RatingDialogArgs.fromBundle(requireArguments()).rating,
                )
            }
        }
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        activity?.let { setBlurView(it, binding.blurView) }
        binding.ivClose.setOnClickListener { dismiss() }

        binding.ratingBar.onRatingBarChangeListener =
            OnRatingBarChangeListener { ratingBar, rating, fromUser ->
                viewModel.score.value = rating.toInt()
            }

        viewModel.msg.observe(viewLifecycleOwner, {
            it?.let { ToastUtil.show(it) }
            dismiss()
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //拿到系统的 bottom_sheet
        val view: FrameLayout = dialog?.findViewById(R.id.design_bottom_sheet)!!
        //设置view高度
        view.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        //获取behavior
        val behavior = BottomSheetBehavior.from(view)
        //设置弹出高度
        behavior.peekHeight = screenHeight
        //设置展开状态
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

}