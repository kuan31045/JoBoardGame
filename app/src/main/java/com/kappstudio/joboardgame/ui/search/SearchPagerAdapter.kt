package com.kappstudio.joboardgame.ui.search

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SearchPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when (SearchType.values()[position]) {
            SearchType.PARTY -> SearchPartyFragment()
            SearchType.GAME -> SearchGameFragment()
            SearchType.USER -> SearchUserFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return SearchType.values()[position].title
    }
}