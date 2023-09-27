package com.kappstudio.joboardgame.data

import androidx.annotation.StringRes

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Fail(@StringRes val stringRes: Int) : Result<Nothing>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[result=$data]"
            is Fail -> "Fail[error=$stringRes]"
            is Error -> "Error[exception=${exception.message}]"
            Loading -> "Loading"
        }
    }
}