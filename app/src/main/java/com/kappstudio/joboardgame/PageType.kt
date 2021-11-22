package com.kappstudio.joboardgame

enum class PageType(val title: String) {
    PARTY(appInstance.getString(R.string.lets_party)),
    GAME(appInstance.getString(R.string.game_viewed)),
    TOOLS(appInstance.getString(R.string.tools)),
    PROFILE(appInstance.getString(R.string.profile)),
    PARTY_DETAIL(""),
    NEW_PARTY(appInstance.getString(R.string.new_party)),
    GAME_DETAIL(""),
    MY_PARTY(appInstance.getString(R.string.my_party)),
    USER(""),
    FAVORITE("史丹利的遊戲收藏"),
    DICE(appInstance.getString(R.string.dice)),
    TIMER(appInstance.getString(R.string.timer)),
    BOTTLE(appInstance.getString(R.string.spin_bottle)),
    MY_RATING("史丹利的遊戲評分"),
    RATING(""),
    ALBUM(appInstance.getString(R.string.album)),
    MAP(""),
    SEARCH(""),
    SELECT_GAME(""),
    NEW_GAME(appInstance.getString(R.string.add_game)),

    OTHER("")
}