package com.kappstudio.joboardgame.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Rating
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.util.*

object JoRemoteDataSource : JoDataSource {

    private const val PATH_PARTIES = "parties"
    private const val PATH_GAMES = "games"
    private const val PATH_USERS = "users"
    private const val PATH_PARTY_MSG = "partyMsgs"
    private const val PATH_RATINGS = "ratings"
    private const val PATH_NOTIFICATIONS = "notifications"
    private const val PATH_REPORTS = "reports"
    private const val KEY_HOST_ID = "hostId"
    private const val KEY_PLAYER_ID_LIST = "playerIdList"
    private const val KEY_PHOTOS = "photos"
    private const val KEY_FRIEND_LIST = "friendList"
    private const val KEY_REQUEST_LIST = "requestList"
    private const val KEY_CREATED_TIME = "createdTime"
    private const val KEY_USER_ID = "userId"

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

    override fun getParties(): MutableLiveData<List<Party>> {
        Timber.d("-----Get Parties------------------------------")

        val liveData = MutableLiveData<List<Party>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PARTIES)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val result = snapshot?.toObjects(Party::class.java) ?: listOf()

                liveData.value = sortParty(result)
            }

        return liveData
    }

    override fun getGames(): MutableLiveData<List<Game>> {
        Timber.d("-----Get Games------------------------------")

        val liveData = MutableLiveData<List<Game>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_GAMES)
            .orderBy(KEY_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                liveData.value = snapshot?.toObjects(Game::class.java) ?: listOf()
            }

        return liveData
    }

    override fun getUsersById(idList: List<String>): MutableLiveData<List<User>> {
        Timber.d("-----Get Users By Id------------------------------")

        val liveData = MutableLiveData<List<User>>()

        FirebaseFirestore.getInstance().collection(PATH_USERS)
            .whereIn("id", idList)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                liveData.value = snapshot?.toObjects(User::class.java) ?: listOf()
            }

        return liveData
    }

    override fun getUser(id: String): MutableLiveData<User> {
        Timber.d("-----Get User------------------------------")

        val liveData = MutableLiveData<User>()

        FirebaseFirestore.getInstance().collection(PATH_USERS)
            .document(id)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                liveData.value = snapshot?.toObject<User>()
            }

        return liveData
    }

    override fun getUserParties(id: String): MutableLiveData<List<Party>> {
        Timber.d("-----Get Users Parties------------------------------")

        val liveData = MutableLiveData<List<Party>>()

        FirebaseFirestore.getInstance().collection(PATH_PARTIES)
            .whereArrayContains(KEY_PLAYER_ID_LIST, id)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val result = snapshot?.toObjects(Party::class.java) ?: listOf()

                liveData.value = sortParty(result)
            }

        return liveData
    }

    override fun getUserHosts(id: String): MutableLiveData<List<Party>> {
        Timber.d("-----Get Users Hosts------------------------------")

        val liveData = MutableLiveData<List<Party>>()

        FirebaseFirestore.getInstance().collection(PATH_PARTIES)
            .whereEqualTo(KEY_HOST_ID, id)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val result = snapshot?.toObjects(Party::class.java) ?: listOf()

                liveData.value = sortParty(result)
            }

        return liveData
    }

    override fun getUserRatings(id: String): MutableLiveData<List<Rating>> {
        Timber.d("-----Get Users Ratings------------------------------")

        val liveData = MutableLiveData<List<Rating>>()

        FirebaseFirestore.getInstance().collection(PATH_RATINGS)
            .whereEqualTo(KEY_USER_ID, id)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                liveData.value = snapshot?.toObjects(Rating::class.java) ?: listOf()
            }

        return liveData
    }

    override suspend fun deleteFavorite(shopId: String): Flow<Result<Boolean>> =
        flow {
            val favorite = FirebaseFirestore.getInstance().collection(FAVORITE)
            val document = favorite.document(shopId)
            document.delete()
            emit(Result.Success(true))
        }.flowOn(Dispatchers.IO).catch { Result.Fail(it.message.toString()) }

    private fun sortParty(parties: List<Party>): List<Party> {
        val openParties =
            parties.filter { it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis }
                .sortedBy { it.partyTime }

        val overParties =
            parties.filter { it.partyTime + 3600000 < Calendar.getInstance().timeInMillis }
                .sortedByDescending { it.partyTime }

        return openParties + overParties
    }
}