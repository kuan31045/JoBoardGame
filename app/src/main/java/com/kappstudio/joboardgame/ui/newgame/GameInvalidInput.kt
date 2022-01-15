package com.kappstudio.joboardgame.ui.newgame

enum class GameInvalidInput(val msg:String) {
    IMAGE_EMPTY("請選擇遊戲圖片!"),
    NAME_EMPTY("請輸入遊戲名!"),
    MIN_PLAYER_QTY_EMPTY("請輸入最小遊玩人數!"),
    MAX_PLAYER_QTY_EMPTY("請輸入最大遊玩人數!"),
    TIME_EMPTY("請輸入遊戲時間!"),
    DESC_EMPTY("請輸入遊戲簡介!"),
    TYPE_EMPTY("請選擇至少一種類型!"),
}