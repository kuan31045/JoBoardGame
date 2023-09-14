package com.kappstudio.joboardgame.data.source.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

interface UserRepository {

    suspend fun login(user: User): Result<Boolean>
}


class UserRepositoryImpl : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun login(user: User): Result<Boolean> = withContext(Dispatchers.IO) {
        Timber.d("----------login----------")

        firestore.collection(COLLECTION_USERS)
            .whereEqualTo(FIELD_ID, user.id)
            .get()
            .addOnCompleteListener { userTask ->
                if (userTask.isSuccessful && !userTask.result.isEmpty) {
                    Result.Success(true)
                }

                firestore.collection(COLLECTION_USERS).document(user.id).set(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Result.Success(true)
                        } else {
                            task.exception?.let {
                                Timber.w(
                                    "[${this::class.simpleName}] Error getting documents. ${it.message}"
                                )
                                return@addOnCompleteListener
                            }
                        }
                    }
            }
        Result.Fail(appInstance.getString(R.string.login_fail))
    }

    private companion object {
        const val COLLECTION_USERS = "users"
        const val FIELD_ID = "id"
    }
}