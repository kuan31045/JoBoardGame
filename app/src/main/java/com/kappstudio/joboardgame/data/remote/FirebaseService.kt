package com.kappstudio.joboardgame.data.remote

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
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.util.ToastUtil
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object FirebaseService {
    private const val COLLECTION_PARTIES = "parties"
    private const val COLLECTION_GAMES = "games"
    private const val COLLECTION_USERS = "users"
    private const val COLLECTION_RATINGS = "ratings"
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
        val newGame = game.copy(id = document.id)


        document
            .set(newGame)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Create Game Successful: $newGame")
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




    suspend fun uploadPhoto(imgUri: Uri): Result<String> =
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
                                continuation.resume(Result.Success(it.result.toString()))
                            } else {
                                task.exception?.let { exception ->

                                    Timber.w("[${this::class.simpleName}] Error uploading img. ${exception.message}")
                                    continuation.resume(Result.Error(exception))
                                    return@let
                                }
                                continuation.resume(
                                    Result.Fail(0)
                                )
                            }
                        }
                    } else {
                        task.exception?.let {
                            Timber.w("[${this::class.simpleName}] Error uploading img. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(0)
                        )
                    }
                }

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


}
