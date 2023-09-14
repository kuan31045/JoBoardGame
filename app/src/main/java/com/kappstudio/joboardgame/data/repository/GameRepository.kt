package com.kappstudio.joboardgame.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.room.GameDao
import com.kappstudio.joboardgame.data.room.toGame
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

interface GameRepository {

    fun getGames(): MutableLiveData<List<Game>>

    fun getLiveGameById(id: String): MutableLiveData<Game>

    suspend fun getRating(game: Game): Rating?

    fun getAllViewedGames(): LiveData<List<Game>>

    suspend fun upsertViewedGame(game: Game)

}


class GameRepositoryImpl(private val gameDao: GameDao) : GameRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val gameCollection = firestore.collection(COLLECTION_GAMES)
    private val ratingCollection = firestore.collection(COLLECTION_RATINGS)

    override fun getGames(): MutableLiveData<List<Game>> {
        Timber.d("----------getGames----------")

        val games = MutableLiveData<List<Game>>()

        gameCollection
            .orderBy(FIELD_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }
                games.value = snapshot?.toObjects(Game::class.java) ?: listOf()
            }

        return games
    }

    override fun getLiveGameById(id: String): MutableLiveData<Game> {
        Timber.d("----------getLiveGameById----------")

        val game = MutableLiveData<Game>()

        gameCollection
            .document(id)
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }
                if (snapshot != null) {
                    game.value = snapshot.toObject<Game>()
                }
            }

        return game
    }

    override suspend fun getRating(game: Game): Rating? = withContext(Dispatchers.IO) {
        Timber.d("----------getRating----------")

        ratingCollection
            .whereEqualTo(FIELD_USER_ID, UserManager.user.value?.id ?: "")
            .whereEqualTo(FIELD_GAME_ID, game.id)
            .get()
            .addOnCompleteListener { task ->
                task.exception?.let { Timber.w("User task failed. ${it.message}") }

                if (task.isSuccessful && !task.result.isEmpty) {
                    task.result.toObjects(Rating::class.java).first()
                }
            }

        null
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