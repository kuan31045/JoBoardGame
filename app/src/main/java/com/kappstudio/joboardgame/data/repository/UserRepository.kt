package com.kappstudio.joboardgame.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Report
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface UserRepository {

    suspend fun addUser(user: User): Boolean

    suspend fun getUsersByIdList(idList: List<String>): Result<List<User>>

    suspend fun getUser(id: String): User

    fun getUserStream(id: String): Flow<User>

    suspend fun insertFavorite(gameMap: HashMap<String, Any>): Boolean

    suspend fun removeFavorite(gameMap: HashMap<String, Any>): Boolean

    suspend fun addMyPhoto(photo: String)

    suspend fun sendReport(report: Report): Boolean
}


class UserRepositoryImpl : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection(COLLECTION_USERS)
    private val reportCollection = firestore.collection(COLLECTION_REPORTS)

    override suspend fun addUser(user: User): Boolean = suspendCoroutine { continuation ->
        userCollection
            .whereEqualTo(FIELD_ID, user.id)
            .get()
            .addOnCompleteListener { userTask ->
                if (!userTask.isSuccessful) {
                    Timber.w("User task failed. ${userTask.exception?.message}")
                    continuation.resume(false)
                }
                if (userTask.result.isEmpty) {

                    userCollection
                        .document(user.id)
                        .set(user)
                        .addOnCompleteListener { registerTask ->
                            registerTask.exception?.let {
                                Timber.w("Register task failed. ${it.message}")
                            }
                            continuation.resume(registerTask.isSuccessful)
                        }

                } else {
                    continuation.resume(true)
                }
            }
    }

    override suspend fun getUsersByIdList(idList: List<String>): Result<List<User>> {
        return try {
            val result = userCollection.whereIn(FIELD_ID, idList).get().await()
            Result.Success(result.toObjects(User::class.java))
        } catch (e: Exception) {
            Timber.w("Error getting documents. $e")
            Result.Fail(R.string.check_internet)
        }
    }

    override suspend fun getUser(id: String): User {
        return try {
            val result = userCollection.document(id).get().await()
            result.toObject(User::class.java)!!
        } catch (e: Exception) {
            Timber.w("Error getting documents. $e")
            User()
        }
    }

    override suspend fun insertFavorite(gameMap: HashMap<String, Any>): Boolean =
        suspendCoroutine { continuation ->
            userCollection
                .document(UserManager.user.value?.id ?: "")
                .update(FIELD_FAVORITE, FieldValue.arrayUnion(gameMap))
                .addOnCompleteListener {
                    continuation.resume(it.isSuccessful)
                }
        }

    override suspend fun removeFavorite(gameMap: HashMap<String, Any>): Boolean =
        suspendCoroutine { continuation ->
            userCollection
                .document(UserManager.user.value?.id ?: "")
                .update(FIELD_FAVORITE, FieldValue.arrayRemove(gameMap))
                .addOnCompleteListener {
                    continuation.resume(it.isSuccessful)
                }
        }

    override suspend fun addMyPhoto(photo: String) {
        userCollection
            .document(UserManager.getUserId())
            .update(
                FIELD_PHOTOS,
                FieldValue.arrayUnion(photo)
            )
    }

    override fun getUserStream(id: String): Flow<User> {
        return userCollection
            .document(id)
            .snapshots()
            .map { it.toObject(User::class.java)!! }
    }

    override suspend fun sendReport(report: Report): Boolean =
        suspendCoroutine { continuation ->

            val newReport = report.copy(
                id = report.id.ifEmpty { reportCollection.document().id }
            )

            reportCollection
                .document(newReport.id)
                .set(newReport)
                .addOnCompleteListener { continuation.resume(it.isSuccessful) }
        }

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_ID = "id"
        const val FIELD_FAVORITE = "favoriteGames"
        const val FIELD_PHOTOS = "photos"
        const val COLLECTION_REPORTS = "reports"
    }
}