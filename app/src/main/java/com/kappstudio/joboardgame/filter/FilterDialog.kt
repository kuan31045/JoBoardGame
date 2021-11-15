package com.kappstudio.joboardgame.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.DialogFilterBinding
import com.kappstudio.joboardgame.databinding.FragmentPartyDetailBinding
import com.kappstudio.joboardgame.game.GameViewModel
import com.kappstudio.joboardgame.party.PartyViewModel
import com.kappstudio.joboardgame.screenHeight
import kotlin.reflect.jvm.internal.impl.descriptors.annotations.BuiltInAnnotationDescriptor

class FilterDialog :  BottomSheetDialogFragment() {
    lateinit var viewModel: GameViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogBg)
        viewModel = ViewModelProvider(requireParentFragment()).get(GameViewModel::class.java)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val binding = DialogFilterBinding.inflate(inflater)
        val list = listOf("陣營","派對","心機","經濟","協商","城市建設","動作反應","耕種","推理")
binding.rvFilter.adapter= FilterAdapter(viewModel).apply {
    submitList(list)
}
        binding.btnFilter.setOnClickListener {
            dismiss()
            viewModel.filter()
        }
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