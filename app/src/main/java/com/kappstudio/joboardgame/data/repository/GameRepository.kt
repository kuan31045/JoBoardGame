package com.kappstudio.joboardgame.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.NewRating
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.room.GameDao
import com.kappstudio.joboardgame.data.room.toGame
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface GameRepository {

    fun getGamesStream(): Flow<List<Game>>

    fun getGameByIdStream(id: String): Flow<Game>

    fun getMyRatingStream(gameId: String): Flow<Rating?>

    fun getAllViewedGamesStream(): Flow<List<Game>>

    suspend fun upsertViewedGame(game: Game)

    suspend fun upsertMyRating(rating: NewRating): Boolean

    suspend fun updateGameRating(rating: Rating, scoreIncrement: Int)

    suspend fun removeMyRating(rating: Rating): Boolean

    suspend fun getGamesByNames(names: List<String>): List<Game>

    fun getUserRatingsStream(userId: String): Flow<List<Rating>>

    suspend fun addGame(game: Game): Boolean

    suspend fun isGameExist(gameName: String): Boolean
}


class GameRepositoryImpl(private val gameDao: GameDao) : GameRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val gameCollection = firestore.collection(COLLECTION_GAMES)
    private val ratingCollection = firestore.collection(COLLECTION_RATINGS)

    override fun getGamesStream(): Flow<List<Game>> {
        return gameCollection
            .snapshots()
            .map {
                it.toObjects(Game::class.java)
            }
    }

    override fun getGameByIdStream(id: String): Flow<Game> {
        return gameCollection
            .document(id)
            .snapshots()
            .map {
                it.toObject(Game::class.java)!!
            }
    }

    override fun getMyRatingStream(gameId: String): Flow<Rating?> {
        return ratingCollection
            .whereEqualTo(FIELD_USER_ID, UserManager.user.value?.id ?: "")
            .whereEqualTo(FIELD_GAME_ID, gameId)
            .snapshots()
            .map {
                it.toObjects(Rating::class.java).firstOrNull()
            }
    }

    override fun getAllViewedGamesStream(): Flow<List<Game>> =
        gameDao.getAllGames().map { entities ->
            entities.map { it.toGame() }
        }

    override suspend fun upsertViewedGame(game: Game) = withContext(Dispatchers.IO) {
        gameDao.upsert(game.toEntity())
    }

    override suspend fun upsertMyRating(rating: NewRating): Boolean =
        suspendCoroutine { continuation ->
            val newRating = rating.copy(
                id = rating.id.ifEmpty { ratingCollection.document().id }
            )

            ratingCollection
                .document(newRating.id)
                .set(newRating)
                .addOnCompleteListener { continuation.resume(it.isSuccessful) }
        }

    override suspend fun updateGameRating(rating: Rating, scoreIncrement: Int) {
        val gameDoc = gameCollection.document(rating.gameId)
        gameDoc.update(FIELD_TOTAL_RATING, FieldValue.increment(scoreIncrement.toDouble()))

        if (rating.id == "") {
            gameDoc.update(FIELD_RATING_QTY, FieldValue.increment(1))
        }
    }

    override suspend fun removeMyRating(rating: Rating): Boolean =
        suspendCoroutine { continuation ->
            val gameDoc = gameCollection.document(rating.gameId)
            gameDoc.update(FIELD_TOTAL_RATING, FieldValue.increment(-(rating.score.toDouble())))
            gameDoc.update(FIELD_RATING_QTY, FieldValue.increment(-1))

            ratingCollection
                .document(rating.id)
                .delete()
                .addOnCompleteListener { continuation.resume(it.isSuccessful) }
        }

    override suspend fun getGamesByNames(names: List<String>): List<Game> {
        return try {
            val result = gameCollection.whereIn(FIELD_NAME, names).get().await()
                .toObjects(Game::class.java)

            val games = mutableListOf<Game>()
            names.forEach { name ->
                games.add(
                    try {
                        result.first { game -> game.name == name }
                    } catch (e: Exception) {
                        Game(name = name)
                    }
                )
            }

            games
        } catch (e: Exception) {
            Timber.w("Error getting documents. $e")
            emptyList()
        }
    }

    override fun getUserRatingsStream(userId: String): Flow<List<Rating>> {
        return ratingCollection
            .whereEqualTo(FIELD_USER_ID, userId)
            .snapshots()
            .map {
                it.toObjects(Rating::class.java)
            }
    }

    override suspend fun addGame(game: Game): Boolean =
        suspendCoroutine { continuation ->
            val newGame = game.copy(
                id = gameCollection.document().id
            )

            gameCollection
                .document(newGame.id)
                .set(newGame)
                .addOnCompleteListener { continuation.resume(it.isSuccessful) }
        }

    override suspend fun isGameExist(gameName: String): Boolean {
        return !gameCollection
            .whereEqualTo(FIELD_NAME, gameName)
            .get()
            .await()
            .isEmpty
    }

    private companion object {
        const val COLLECTION_GAMES = "games"
        const val FIELD_CREATED_TIME = "createdTime"
        const val COLLECTION_RATINGS = "ratings"
        const val FIELD_USER_ID = "userId"
        const val FIELD_GAME_ID = "gameId"
        const val FIELD_TOTAL_RATING = "totalRating"
        const val FIELD_RATING_QTY = "ratingQty"
        const val FIELD_NAME = "name"
    }
}