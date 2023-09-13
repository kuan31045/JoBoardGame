package com.kappstudio.joboardgame.ui.tools.drawlots

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import androidx.fragment.app.DialogFragment
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.databinding.DialogDrawBinding

class DrawDialog : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ThemeOverlay_AppCompat_Dialog_Alert)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = DialogDrawBinding.inflate(inflater, container, false)
        val item = DrawDialogArgs.fromBundle(requireArguments()).item
        val animSet = AnimationSet(true)

        animSet.fillAfter = true
        animSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.amin_scale))
        animSet.addAnimation(AnimationUtils.loadAnimation(context, R.anim.amin_rotate))

        binding.lottieDraw.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {}

            override fun onAnimationEnd(p0: Animator) {
                binding.tvTitleResult.visibility = View.VISIBLE
                binding.tvResult.text = item
                binding.tvResult.startAnimation(animSet)
            }

            override fun onAnimationCancel(p0: Animator) {}

            override fun onAnimationRepeat(p0: Animator) {}
        })

        return binding.root
    }
}