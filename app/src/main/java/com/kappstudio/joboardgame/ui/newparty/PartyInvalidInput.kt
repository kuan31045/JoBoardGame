package com.kappstudio.joboardgame.ui.newparty

import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance

enum class PartyInvalidInput(val msg:String) {
    TITLE_EMPTY(appInstance.getString(R.string.enter_title)),
    TIME_EMPTY(appInstance.getString(R.string.enter_time)),
    LOCATION_EMPTY(appInstance.getString(R.string.enter_place)),
    QTY_EMPTY(appInstance.getString(R.string.enter_people_qty)),
    DESC_EMPTY(appInstance.getString(R.string.enter_desc)),
    GAMES_EMPTY(appInstance.getString(R.string.enter_game)),
}