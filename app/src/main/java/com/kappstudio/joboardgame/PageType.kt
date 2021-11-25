package com.kappstudio.joboardgame

enum class PageType(val title: String) {
    PARTY(appInstance.getString(R.string.lets_party)),
    GAME(appInstance.getString(R.string.game_viewed)),
    TOOLS(appInstance.getString(R.string.tools)),
    PROFILE(appInstance.getString(R.string.profile)),
    PARTY_DETAIL(""),
    NEW_PARTY(appInstance.getString(R.string.new_party)),
    GAME_DETAIL(""),
    USER(""),
    DICE(appInstance.getString(R.string.dice)),
    TIMER(appInstance.getString(R.string.timer)),
    BOTTLE(appInstance.getString(R.string.spin_bottle)),
    DRAW_LOTS(appInstance.getString(R.string.draw_lots)),
    SCOREBOARD(appInstance.getString(R.string.scoreboard)),
    REPORT(""),

    POLYGRAPH(appInstance.getString(R.string.polygraph)),

    RATING(""),
    ALBUM(appInstance.getString(R.string.album)),
    MAP(""),
    SEARCH(""),
    SELECT_GAME(""),
    NEW_GAME(appInstance.getString(R.string.add_game)),
    NOTIFICATION(appInstance.getString(R.string.notification)),

    //TODO Show the user's name
    FAVORITE("收藏"),
    FRIEND_LIST("好友名單"),
    MY_RATING("評分"),
    MY_PARTY("參加的聚會"),
    MY_HOST("主辦的聚會"),

    OTHER("")
}