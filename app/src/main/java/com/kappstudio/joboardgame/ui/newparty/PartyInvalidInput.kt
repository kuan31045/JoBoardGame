package com.kappstudio.joboardgame.ui.newparty

import androidx.annotation.StringRes
import com.kappstudio.joboardgame.R

enum class PartyInvalidInput(@StringRes val stringRes: Int) {
    TITLE_EMPTY(R.string.enter_title),
    TIME_EMPTY(R.string.enter_time),
    LOCATION_EMPTY(R.string.enter_place),
    QTY_EMPTY(R.string.enter_people_qty),
    DESC_EMPTY(R.string.enter_desc),
    GAMES_EMPTY(R.string.enter_game),
}