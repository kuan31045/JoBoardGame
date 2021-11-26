package com.kappstudio.joboardgame.data.source.remote

import androidx.lifecycle.LiveData
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.source.JoDataSource

object JoRemoteDataSource : JoDataSource {
    private const val COLLECTION_PARTIES = "parties"
    private const val COLLECTION_GAMES = "games"
    private const val COLLECTION_USERS = "users"
    private const val COLLECTION_PARTY_MSG = "partyMsgs"
    private const val COLLECTION_RATINGS = "ratings"
    private const val COLLECTION_NOTIFICATIONS = "notifications"
    private const val COLLECTION_REPORTS = "reports"
    private const val FIELD_PLAYER_ID_LIST = "playerIdList"
    private const val FIELD_PHOTOS = "photos"
    private const val FIELD_FRIEND_LIST = "friendList"
    private const val FIELD_REQUEST_LIST = "requestList"
    override suspend fun getViewedGame(id: String): Game? {
        TODO("Not yet implemented")
    }

    override suspend fun insertViewedGame(game: Game) {
        TODO("Not yet implemented")
    }

    override suspend fun updateViewedGame(game: Game) {
        TODO("Not yet implemented")
    }

    override fun getAllViewedGames(): LiveData<List<Game>> {
        TODO("Not yet implemented")
    }


}