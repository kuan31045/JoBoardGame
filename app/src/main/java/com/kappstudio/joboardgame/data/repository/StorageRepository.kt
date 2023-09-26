package com.kappstudio.joboardgame.data.repository

import android.net.Uri
import com.kappstudio.joboardgame.data.Result

interface StorageRepository {
    suspend fun uploadPhoto(imgUri: Uri): Result<String>
}


class StorageRepositoryImpl:StorageRepository{
    override suspend fun uploadPhoto(imgUri: Uri): Result<String> {
        TODO("Not yet implemented")
    }

}