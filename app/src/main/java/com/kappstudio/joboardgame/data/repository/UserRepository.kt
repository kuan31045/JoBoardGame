package com.kappstudio.joboardgame.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface UserRepository {

    suspend fun addUser(user: User): Boolean

    suspend fun getUsersByIdList(idList: List<String>): Result<List<User>>

    suspend fun getUser(id: String): User

    suspend fun insertFavorite(gameMap: HashMap<String, Any>): Boolean

    suspend fun removeFavorite(gameMap: HashMap<String, Any>): Boolean
}


class UserRepositoryImpl : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection(COLLECTION_USERS)

    override suspend fun addUser(user: User): Boolean = suspendCoroutine { continuation ->
        Timber.d("----------login----------")

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
        Timber.d("----------getUsersByIdList----------")

        return try {
            val result = userCollection.whereIn(FIELD_ID, idList).get().await()
            Result.Success(result.toObjects(User::class.java))
        } catch (e: Exception) {
            Timber.w("Error getting documents. $e")
            Result.Fail(R.string.check_internet)
        }
    }

    override suspend fun getUser(id: String): User {
        Timber.d("----------getUser----------")

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
            Timber.d("----------insertFavorite----------")

            userCollection
                .document(UserManager.user.value?.id ?: "")
                .update(FIELD_FAVORITE, FieldValue.arrayUnion(gameMap))
                .addOnCompleteListener {
                    continuation.resume(it.isSuccessful)
                }
        }

    override suspend fun removeFavorite(gameMap: HashMap<String, Any>): Boolean =
        suspendCoroutine { continuation ->
            Timber.d("----------removeFavorite----------")

            userCollection
                .document(UserManager.user.value?.id ?: "")
                .update(FIELD_FAVORITE, FieldValue.arrayRemove(gameMap))
                .addOnCompleteListener {
                    continuation.resume(it.isSuccessful)
                }
        }

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_ID = "id"
        const val FIELD_FAVORITE = "favoriteGames"
    }
}