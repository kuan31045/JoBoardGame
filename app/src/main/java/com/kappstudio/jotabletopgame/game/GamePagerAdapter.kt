package com.kappstudio.jotabletopgame.game

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.game.all.AllGameFragment
import com.kappstudio.jotabletopgame.game.featured.FeaturedGameFragment

private val TAB_TITLES = arrayOf(
    R.string.featured,
    R.string.all,
)

class GamePagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> FeaturedGameFragment()
            1 -> AllGameFragment()
            else -> AllGameFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return appInstance.getString(TAB_TITLES[position])
    }
}
