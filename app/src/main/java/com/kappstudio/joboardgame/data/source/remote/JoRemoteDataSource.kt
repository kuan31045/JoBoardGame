package com.kappstudio.joboardgame.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.data.source.JoDataSource
import com.kappstudio.joboardgame.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
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
    private const val KEY_PARTY_ID = "partyId"
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

    override fun getParty(id: String): MutableLiveData<Party> {
        Timber.d("-----Get Party------------------------------")

        val liveData = MutableLiveData<Party>()

        FirebaseFirestore.getInstance().collection(PATH_PARTIES)
            .document(id)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                liveData.value = snapshot?.toObject<Party>()
            }

        return liveData
    }

    override fun getPartyMsgs(id: String): MutableLiveData<List<PartyMsg>> {
        Timber.d("-----Get Party Msgs------------------------------")

        val liveData = MutableLiveData<List<PartyMsg>>()

        FirebaseFirestore.getInstance().collection(PATH_PARTY_MSG)
            .whereEqualTo(KEY_PARTY_ID, id)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val list = snapshot?.toObjects(PartyMsg::class.java) ?: listOf()
                liveData.value = list.sortedByDescending { it.createdTime }
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

    override fun getGamesByNames(names: List<String>): MutableLiveData<List<Game>> {
        Timber.d("-----Get Games By Names------------------------------")

        val liveData = MutableLiveData<List<Game>>()

        FirebaseFirestore.getInstance().collection(PATH_GAMES)
            .whereIn("name", names)
            .addSnapshotListener { snapshot, exception ->

                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val result = snapshot?.toObjects(Game::class.java) ?: listOf()

                val list = mutableListOf<Game>()
                names.forEach { name ->
                    list.add(
                        try {
                            result.first { it.name == name }
                        } catch (e: Exception) {
                            Game(name = name)
                        }
                    )
                }

                liveData.value = list
            }

        return liveData
    }

    override fun getUsersByIdList(idList: List<String>): MutableLiveData<List<User>> {
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

    override suspend fun joinParty(id: String): Flow<Resource<Boolean>> =
        flow {
            Timber.d("-----Join Party------------------------------")

            FirebaseFirestore.getInstance()
                .collection(PATH_PARTIES)
                .document(id)
                .update(
                    KEY_PLAYER_ID_LIST,
                    FieldValue.arrayUnion(UserManager.user.value?.id ?: "")
                )

            emit(Resource.Success(true))

        }.flowOn(Dispatchers.IO).catch {
            Resource.Fail(it.message.toString())
        }

    override suspend fun leaveParty(id: String): Flow<Resource<Boolean>> =
        flow {
            Timber.d("-----Leave Party------------------------------")

            FirebaseFirestore.getInstance()
                .collection(PATH_PARTIES)
                .document(id)
                .update(
                    KEY_PLAYER_ID_LIST,
                    FieldValue.arrayRemove(UserManager.user.value?.id ?: "")
                )

            emit(Resource.Success(true))

        }.flowOn(Dispatchers.IO).catch {
            Resource.Fail(it.message.toString())
        }

    override suspend fun insertFavorite(gameMap: HashMap<String, Any>): Flow<Resource<Boolean>> =
        flow {
            Timber.d("-----Insert Favorite------------------------------")

            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .document(UserManager.user.value?.id ?: "")
                .update("favoriteGames", FieldValue.arrayUnion(gameMap))

            emit(Resource.Success(true))

        }.flowOn(Dispatchers.IO).catch {
            Resource.Fail(it.message.toString())
        }

    override suspend fun removeFavorite(gameMap: HashMap<String, Any>): Flow<Resource<Boolean>> =
        flow {
            Timber.d("-----Delete Favorite------------------------------")

            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .document(UserManager.user.value?.id ?: "")
                .update("favoriteGames", FieldValue.arrayRemove(gameMap))

            emit(Resource.Success(true))

        }.flowOn(Dispatchers.IO).catch {
            Resource.Fail(it.message.toString())
        }

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