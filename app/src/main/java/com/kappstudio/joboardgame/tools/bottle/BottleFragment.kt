package com.kappstudio.joboardgame.tools.bottle

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationSet
import android.view.animation.RotateAnimation
import com.kappstudio.joboardgame.databinding.FragmentBottleBinding

class BottleFragment : Fragment() {

    lateinit var binding: FragmentBottleBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottleBinding.inflate(inflater)
        binding.btnSpin.setOnClickListener {
            val animSet = AnimationSet(true)
            animSet.fillAfter = true //動畫結束時，畫面停在動畫結束的最後一個畫面

            val degreesRange = 0..360
            val randomDegrees = degreesRange.random().toFloat()
            val animation = RotateAnimation(
                0f, //起始角度
                3600f + randomDegrees, //結束角度
                RotateAnimation.RELATIVE_TO_SELF, //pivotXType
                0.5f, //設定x旋轉中心點
                RotateAnimation.RELATIVE_TO_SELF,
                0.5f  //設定y旋轉中心點
            )
            //動畫持續時間
            animation.duration = 4500
            //將圖片載入動畫

            animSet.addAnimation(animation)

            this.binding.ivBottle.startAnimation(animSet)
        }

        return binding.root
    }
}