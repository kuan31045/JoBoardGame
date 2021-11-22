package com.kappstudio.joboardgame.search

import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance

enum class SearchType( val title: String) {
    PARTY(  appInstance.getString(R.string.party)),
    GAME(  appInstance.getString(R.string.game)),
    USER(  appInstance.getString(R.string.user))

}