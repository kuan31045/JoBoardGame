package com.kappstudio.jotabletopgame.util

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.google.android.material.internal.ContextUtils.getActivity
import com.kappstudio.jotabletopgame.appInstance
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur

fun setBlurView(activity: Activity,blurView:BlurView) {
    val radius = 20f

    val decorView: View? = activity.getWindow()?.getDecorView()
    //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
    //ViewGroup you want to start blur from. Choose root as close to BlurView in hierarchy as possible.
    val rootView = decorView?.findViewById<View>(android.R.id.content) as ViewGroup
    //Set drawable to draw in the beginning of each blurred frame (Optional).
    //Can be used in case your layout has a lot of transparent space and your content
    //gets kinda lost after after blur is applied.
    //Set drawable to draw in the beginning of each blurred frame (Optional).
    //Can be used in case your layout has a lot of transparent space and your content
    //gets kinda lost after after blur is applied.
    val windowBackground = decorView?.background

    blurView.setupWith(rootView)
        .setFrameClearDrawable(windowBackground)
        .setBlurAlgorithm(RenderScriptBlur(appInstance))
        .setBlurRadius(radius)
        .setBlurAutoUpdate(true)
        .setHasFixedTransformationMatrix(true) // Or false if it's in a scrolling container or might be animated

}