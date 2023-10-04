package com.kappstudio.joboardgame.ui.newgame

import androidx.annotation.StringRes
import com.kappstudio.joboardgame.R

enum class GameInvalidInput(@StringRes val stringRes: Int) {
    IMAGE_EMPTY(R.string.plz_pick_image),
    NAME_EMPTY(R.string.plz_enter_game_name),
    MIN_PLAYER_QTY_EMPTY(R.string.plz_enter_min_player),
    MAX_PLAYER_QTY_EMPTY(R.string.plz_enter_max_player),
    TIME_EMPTY(R.string.plz_enter_game_time),
    DESC_EMPTY(R.string.plz_enter_game_desc),
    TYPE_EMPTY(R.string.plz_pick_type)
}