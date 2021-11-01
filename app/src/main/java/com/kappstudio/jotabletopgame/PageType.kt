package com.kappstudio.jotabletopgame

enum class PageType(val title: String) {
    PARTY(""),
    GAME(appInstance.getString(R.string.game_viewed)),
    TOOLS(appInstance.getString(R.string.tools)),
    PROFILE(appInstance.getString(R.string.profile)),
    PARTY_DETAIL(""),
    NEW_PARTY(appInstance.getString(R.string.new_party)),
    GAME_DETAIL(""),
    MY_PARTY(appInstance.getString(R.string.my_party)),
    USER(""),
    FAVORITE(appInstance.getString(R.string.my_favorite)),
    DICE(appInstance.getString(R.string.dice)),
    TIMER(appInstance.getString(R.string.timer)),
    BOTTLE(appInstance.getString(R.string.spin_bottle)),


    OTHER("")
}