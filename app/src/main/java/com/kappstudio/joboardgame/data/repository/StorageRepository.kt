package com.kappstudio.joboardgame.data.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.util.ConnectivityUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.Calendar

interface StorageRepository {

    suspend fun uploadPhoto(fileUri: Uri): Flow<Result<String>>
}


class StorageRepositoryImpl : StorageRepository {

    private val storageReference = FirebaseStorage.getInstance().reference

    override suspend fun uploadPhoto(fileUri: Uri): Flow<Result<String>> = flow {
        emit(Result.Loading)

        if (ConnectivityUtil.isNotConnected()) {
            emit(Result.Fail(R.string.check_internet))
        }

        val uploadTime = Calendar.getInstance().timeInMillis
        val fileName = (UserManager.getUserId()) + uploadTime

        val uri =
            storageReference
                .child("${PATH_PHOTOS}/${UserManager.getUserId()}/$fileName")
                .putFile(fileUri).await()
                .task.result.storage.downloadUrl.await() ?: ""

        emit(Result.Success(uri.toString()))

    }.catch {
        Timber.w("Upload photo failed. ${it.message}")
        emit(Result.Fail(R.string.upload_fail))
    }.flowOn(Dispatchers.IO)

    private companion object {
        const val PATH_PHOTOS = "photos"
    }
}