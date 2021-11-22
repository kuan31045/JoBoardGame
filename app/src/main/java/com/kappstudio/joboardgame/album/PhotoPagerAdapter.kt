package com.kappstudio.joboardgame.album

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import android.view.MotionEvent
import java.lang.IllegalArgumentException


class PhotoPagerAdapter(
    fm: FragmentManager,
    private val photos: Array<String>,
) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return photos.size
    }

    override fun getItem(position: Int): Fragment {
        return PhotoItemFragment(photos[position])
    }

}