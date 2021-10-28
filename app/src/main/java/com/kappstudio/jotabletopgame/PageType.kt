package com.kappstudio.jotabletopgame

enum class PageType(val title: String) {
    PARTY(""),
    GAME(appInstance.getString(R.string.game_viewed)),
    TOOL(appInstance.getString(R.string.tool)),
    PROFILE(appInstance.getString(R.string.profile)),
    PARTY_DETAIL(""),
    NEW_PARTY(appInstance.getString(R.string.new_party)),
    GAME_DETAIL(""),
    MY_PARTY(appInstance.getString(R.string.my_party)),
    USER(""),

    OTHER("")
}