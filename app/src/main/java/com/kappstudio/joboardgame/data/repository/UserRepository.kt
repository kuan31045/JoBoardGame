package com.kappstudio.joboardgame.data.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.remote.JoRemoteDataSource
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.HashMap

interface UserRepository {

    suspend fun login(user: User): Result<Boolean>

    fun getUsersByIdList(idList: List<String>): MutableLiveData<List<User>>

    suspend fun insertFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>>

    suspend fun removeFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>>
}


class UserRepositoryImpl : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val userCollection = firestore.collection(COLLECTION_USERS)

    override suspend fun login(user: User): Result<Boolean> = withContext(Dispatchers.IO) {
        Timber.d("----------login----------")

        userCollection
            .whereEqualTo(FIELD_ID, user.id)
            .get()
            .addOnCompleteListener { userTask ->
                if (!userTask.isSuccessful) {
                    Timber.w("User task failed. ${userTask.exception?.message}")
                    Result.Fail(appInstance.getString(R.string.login_fail))
                }
                if (userTask.result.isEmpty) {
                    firestore.collection(COLLECTION_USERS).document(user.id).set(user)
                        .addOnCompleteListener { loginTask ->
                            if (!loginTask.isSuccessful) {
                                Timber.w("Login task failed. ${loginTask.exception?.message}")
                                Result.Fail(appInstance.getString(R.string.login_fail))
                            }
                        }
                }
            }
        Result.Success(true)
    }

    override fun getUsersByIdList(idList: List<String>): MutableLiveData<List<User>> {
        Timber.d("----------getUsersByIdList----------")

        val users = MutableLiveData<List<User>>()

        userCollection
            .whereIn(FIELD_ID, idList)
            .addSnapshotListener { snapshot, exception ->
                exception?.let { Timber.w("Error getting documents. ${it.message}") }
                users.value = snapshot?.toObjects(User::class.java) ?: listOf()
            }

        return users
    }

    override suspend fun insertFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>> =
        flow {
            Timber.d("----------insertFavorite----------")

            userCollection
                .document(UserManager.user.value?.id ?: "")
                .update(FIELD_FAVORITE, FieldValue.arrayUnion(gameMap))

            emit(Result.Success(true))

        }.flowOn(Dispatchers.IO).catch {
            Timber.w("Update failed. ${it.message}")
            Result.Fail(appInstance.getString(R.string.insert_fail))
        }

    override suspend fun removeFavorite(gameMap: HashMap<String, Any>): Flow<Result<Boolean>> =
        flow {
            Timber.d("----------removeFavorite----------")

            userCollection
                .document(UserManager.user.value?.id ?: "")
                .update(FIELD_FAVORITE, FieldValue.arrayRemove(gameMap))

            emit(Result.Success(true))

        }.flowOn(Dispatchers.IO).catch {
            Timber.w("Update failed. ${it.message}")
            Result.Fail(appInstance.getString(R.string.remove_fail))
        }

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_ID = "id"
        const val FIELD_FAVORITE = "favoriteGames"
    }
}