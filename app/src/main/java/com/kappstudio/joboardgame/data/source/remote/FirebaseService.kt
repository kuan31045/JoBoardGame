package com.kappstudio.joboardgame.data.source.remote

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.*
import com.kappstudio.joboardgame.login.UserManager
import tech.gujin.toast.ToastUtil
import timber.log.Timber
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



object FirebaseService {
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
    suspend fun sendReport(report: Report): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Send Report------------------------------")

        val reports = FirebaseFirestore.getInstance().collection(COLLECTION_REPORTS)
        val document = reports.document()

        report.id = document.id

        document
            .set(report)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Create Party Successful: $report")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    fun newFriend(userId: String) {
        Timber.d("-----New Friend------------------------------")

        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).document(userId)
            .update(FIELD_FRIEND_LIST, FieldValue.arrayUnion(UserManager.user.value?.id ?: ""))

        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).document(UserManager.user.value?.id ?: "")
            .update(FIELD_FRIEND_LIST, FieldValue.arrayUnion(userId))

        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).document(UserManager.user.value?.id ?: "")
            .update(FIELD_REQUEST_LIST, FieldValue.arrayRemove(userId))
    }

    fun refuseRequest(userId: String) {
        Timber.d("-----Refuse Request-----------------------------")

        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).document(UserManager.user.value?.id ?: "")
            .update(FIELD_REQUEST_LIST, FieldValue.arrayRemove(userId))
    }

    fun sendFriendRequest(userId: String) {
        Timber.d("-----Send Friend Request------------------------------")
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).document(userId)
            .update(FIELD_REQUEST_LIST, FieldValue.arrayUnion(UserManager.user.value?.id ?: ""))

        ToastUtil.show(appInstance.getString(R.string.send_request_ok))
    }


    suspend fun createGame(game: Game): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Create Game------------------------------")

        val games = FirebaseFirestore.getInstance().collection(COLLECTION_GAMES)
        val document = games.document()

        game.id = document.id

        document
            .set(game)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Create Game Successful: $game")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    suspend fun addUser(user: User): Resource<Boolean> =
        suspendCoroutine { continuation ->
            Timber.d("-----Add User------------------------------")

            val query = FirebaseFirestore.getInstance()

            FirebaseFirestore.getInstance().collection(COLLECTION_USERS)
                .whereEqualTo("id", user.id)
                .get()
                .addOnCompleteListener { userTask ->
                    if (userTask.isSuccessful && userTask.result.isEmpty) {

                        val postUser = query.collection(COLLECTION_USERS).document(user.id)

                        postUser.set(user)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    continuation.resume(Resource.Success(true))
                                } else {
                                    task.exception?.let {
                                        Timber.w(
                                            "[${this::class.simpleName}] Error getting documents. ${it.message}"
                                        )
                                        continuation.resume(Resource.Error(it))
                                        return@addOnCompleteListener
                                    }
                                    continuation.resume(
                                        Resource.Fail(
                                            appInstance.getString(
                                                R.string.nothing
                                            )
                                        )
                                    )
                                }
                            }
                    } else {
                        continuation.resume(Resource.Success(true))
                    }
                }
        }

    fun getLiveUser(userId: String): MutableLiveData<User> {
        Timber.d("-----Get Live User By Id------------------------------")

        val user = MutableLiveData<User>()

        FirebaseFirestore.getInstance().collection(COLLECTION_USERS).document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    user.value = snapshot.toObject<User>()
                } else {
                    Timber.d("Current data: null")
                }
            }

        return user
    }

    suspend fun addPartyPhoto(partyId: String, photo: String): Resource<String> =
        suspendCoroutine { continuation ->
            Timber.d("-----Add Party Photo------------------------------")
            FirebaseFirestore.getInstance()
                .collection(COLLECTION_PARTIES).document(partyId)
                .update(FIELD_PHOTOS, FieldValue.arrayUnion(photo))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        FirebaseFirestore.getInstance()
                            .collection(COLLECTION_USERS).document(UserManager.user.value?.id ?: "")
                            .update(FIELD_PHOTOS, FieldValue.arrayUnion(photo))

                        continuation.resume(Resource.Success("success"))
                    } else {
                        task.exception?.let {
                            Timber.w("[${this::class.simpleName}] Error adding photo. ${it.message}")
                            continuation.resume(Resource.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Resource.Fail(appInstance.getString(R.string.nothing))
                        )
                    }
                }
        }

    suspend fun uploadPhoto(imgUri: Uri): Resource<String> =
        suspendCoroutine { continuation ->
            Timber.d("-----Upload Photo------------------------------")

            val uploadTime = Calendar.getInstance().timeInMillis
            val fileName = (UserManager.user.value?.id ?: "") + uploadTime

            val storageRef =
                FirebaseStorage.getInstance().reference
                    .child("$FIELD_PHOTOS/${UserManager.user.value?.id ?: ""}/$fileName")

            storageRef.putFile(imgUri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.storage.downloadUrl.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Timber.d("Upload Photo Success: $fileName")
                                continuation.resume(Resource.Success(it.result.toString()))
                            } else {
                                task.exception?.let { exception ->

                                    Timber.w("[${this::class.simpleName}] Error uploading img. ${exception.message}")
                                    continuation.resume(Resource.Error(exception))
                                    return@let
                                }
                                continuation.resume(
                                    Resource.Fail(appInstance.getString(R.string.nothing))
                                )
                            }
                        }
                    } else {
                        task.exception?.let {
                            Timber.w("[${this::class.simpleName}] Error uploading img. ${it.message}")
                            continuation.resume(Resource.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Resource.Fail(appInstance.getString(R.string.nothing))
                        )
                    }
                }

        }

    suspend fun removeRating(rating: Rating): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Remove Rating------------------------------")
        val game = FirebaseFirestore.getInstance()
            .collection(COLLECTION_GAMES).document(rating.gameId)
        game.update("totalRating", FieldValue.increment(-(rating.score.toDouble())))
        game.update("ratingQty", FieldValue.increment(-1))

        FirebaseFirestore.getInstance().collection(COLLECTION_RATINGS).document(rating.id)
            .delete()
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener {
                continuation.resume(false)
            }

    }

    fun updateRating(rating: Rating, addScore: Int) {
        val game = FirebaseFirestore.getInstance()
            .collection(COLLECTION_GAMES).document(rating.gameId)
        game.update("totalRating", FieldValue.increment(addScore.toDouble()))

        if (rating.id == "") {
            game.update("ratingQty", FieldValue.increment(1))
        }

    }

    suspend fun sendRating(rating: NewRating): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Send Rating------------------------------")

        val ratings = FirebaseFirestore.getInstance().collection(COLLECTION_RATINGS)
        if (rating.id.isEmpty()) {
            rating.id = ratings.document().id
        }

        ratings.document(rating.id)
            .set(rating)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Send Party Msg Successful: $rating")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    suspend fun getRating(game: Game): Rating =
        suspendCoroutine { continuation ->
            Timber.d("-----Get My Rating------------------------------")

            FirebaseFirestore.getInstance()
                .collection(COLLECTION_RATINGS)
                .whereEqualTo("userId", UserManager.user.value?.id ?: "")
                .whereEqualTo("gameId", game.id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.size() != 0) {
                            Timber.d("Has Rating: ${game.name}")
                            val rating =
                                task.result?.toObjects(Rating::class.java)?.first() ?: Rating()

                            continuation.resume(rating)
                        } else {
                            Timber.d("Has Not Rating: ${game.name}")
                            continuation.resume(
                                Rating(
                                    gameId = game.id,
                                    game = game,
                                    userId = UserManager.user.value?.id ?: ""
                                )
                            )
                        }

                    } else {
                        task.exception?.let {

                            Timber.w("Error getting documents. ${it.message}")
                            return@addOnCompleteListener
                        }
                    }
                }
        }




 

    fun addToFavorite(gameMap: HashMap<String, Any>) {
        Timber.d("-----Add To Favorite------------------------------")
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).document(UserManager.user.value?.id ?: "")
            .update("favoriteGames", FieldValue.arrayUnion(gameMap))

        ToastUtil.show(appInstance.getString(R.string.favorite_in))
    }

    fun removeFavorite(gameMap: HashMap<String, Any>) {
        Timber.d("-----Remove Favorite------------------------------")
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).document(UserManager.user.value?.id ?: "")
            .update("favoriteGames", FieldValue.arrayRemove(gameMap))


        ToastUtil.show(appInstance.getString(R.string.favorite_out))
    }

    fun getLivePartyMsgs(id: String): MutableLiveData<List<PartyMsg>> {
        Timber.d("-----Get Live Party Msgs------------------------------")


        val liveData = MutableLiveData<List<PartyMsg>>()

        FirebaseFirestore.getInstance()
            .collection("partyMsgs").whereEqualTo("partyId", id)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                liveData.value = snapshot?.toObjects(PartyMsg::class.java) ?: mutableListOf()
                Timber.d("Current data: ${liveData.value}")
            }

        return liveData
    }

    suspend fun sendPartyMsg(msg: PartyMsg): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Send Party Msg------------------------------")

        val msgs = FirebaseFirestore.getInstance().collection("partyMsgs")
        val document = msgs.document()

        msg.id = document.id

        document
            .set(msg)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Send Party Msg Successful: $msg")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    fun getLiveGameById(gameId: String): MutableLiveData<Game> {
        Timber.d("-----Get Live Game By Id------------------------------")

        val game = MutableLiveData<Game>()

        FirebaseFirestore.getInstance().collection(COLLECTION_GAMES).document(gameId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    game.value = snapshot.toObject<Game>()?.apply {
                        viewedTime = Calendar.getInstance().timeInMillis
                    }
                } else {
                    Timber.d("Current data: null")
                }
            }

        return game
    }



    suspend fun createParty(party: Party): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Create Party------------------------------")

        val parties = FirebaseFirestore.getInstance().collection(COLLECTION_PARTIES)
        val document = parties.document()

        party.id = document.id

        document
            .set(party)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Create Party Successful: $party")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    fun getLivePartyById(partyId: String): MutableLiveData<Party> {
        Timber.d("-------------------------------")

        val party = MutableLiveData<Party>()

        FirebaseFirestore.getInstance().collection(COLLECTION_PARTIES).document(partyId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    party.value = snapshot.toObject<Party>()
                } else {
                    Timber.d("Current data: null")
                }
            }

        return party
    }


    fun getLiveUsers(): MutableLiveData<List<User>> {
        Timber.d("-----Get All Live Users------------------------------")

        val liveData = MutableLiveData<List<User>>()

        FirebaseFirestore.getInstance()
            .collection(COLLECTION_USERS).orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                liveData.value = snapshot?.toObjects(User::class.java) ?: mutableListOf()
                Timber.d("Current data: ${liveData.value}")
            }
        return liveData
    }



    fun leaveParty(partyId: String) {
        Timber.d("-----Leave Party------------------------------")
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_PARTIES).document(partyId)
            .update(FIELD_PLAYER_ID_LIST, FieldValue.arrayRemove(UserManager.user.value?.id ?: ""))

        ToastUtil.show(appInstance.getString(R.string.bye))
    }

    fun joinParty(partyId: String) {
        Timber.d("-----Join Party------------------------------")
        FirebaseFirestore.getInstance()
            .collection(COLLECTION_PARTIES).document(partyId)
            .update(FIELD_PLAYER_ID_LIST, FieldValue.arrayUnion(UserManager.user.value?.id ?: ""))

        ToastUtil.show(appInstance.getString(R.string.welcome))
    }


}
