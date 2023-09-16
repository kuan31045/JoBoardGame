package com.kappstudio.joboardgame.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.room.GameDao
import com.kappstudio.joboardgame.data.room.toGame
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import timber.log.Timber

interface GameRepository {

    fun getGames(): Flow<List<Game>>

    fun getGameById(id: String): Flow<Game>

    fun getMyRating(gameId: String): Flow<Rating?>

    fun getAllViewedGames(): LiveData<List<Game>>

    suspend fun upsertViewedGame(game: Game)
}


class GameRepositoryImpl(private val gameDao: GameDao) : GameRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val gameCollection = firestore.collection(COLLECTION_GAMES)
    private val ratingCollection = firestore.collection(COLLECTION_RATINGS)

    override fun getGames(): Flow<List<Game>> {
        Timber.d("----------getGames----------")

        return gameCollection
            .snapshots()
            .map {
                it.toObjects(Game::class.java)
            }
    }

    override fun getGameById(id: String): Flow<Game> {
        Timber.d("----------getGameById----------")

        return gameCollection
            .document(id)
            .snapshots()
            .map {
                it.toObject(Game::class.java)!!
            }
    }

    override fun getMyRating(gameId: String): Flow<Rating?> {
        Timber.d("----------getMyRating----------")

        return ratingCollection
            .whereEqualTo(FIELD_USER_ID, UserManager.user.value?.id ?: "")
            .whereEqualTo(FIELD_GAME_ID, gameId)
            .snapshots()
            .map {
                it.toObjects(Rating::class.java).firstOrNull()
            }
    }

    override fun getAllViewedGames(): LiveData<List<Game>> = gameDao.getAllGames().map { entities ->
        entities.map { it.toGame() }
    }

    override suspend fun upsertViewedGame(game: Game) = withContext(Dispatchers.IO) {
        gameDao.upsert(game.toEntity())
    }

    private companion object {
        const val COLLECTION_GAMES = "games"
        const val FIELD_CREATED_TIME = "createdTime"
        const val COLLECTION_RATINGS = "ratings"
        const val FIELD_USER_ID = "userId"
        const val FIELD_GAME_ID = "gameId"
    }
}