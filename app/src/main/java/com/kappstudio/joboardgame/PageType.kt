package com.kappstudio.joboardgame

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
    MY_RATING(appInstance.getString(R.string.my_rating)),
    RATING(""),
    ALBUM(appInstance.getString(R.string.album)),
    MAP(""),


    OTHER("")
}