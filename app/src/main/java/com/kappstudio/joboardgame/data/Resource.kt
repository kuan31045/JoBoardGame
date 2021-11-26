package com.kappstudio.joboardgame.data

sealed class Resource<out R> {

    data class Success<out T>(val data: T) : Resource<T>()
    data class Fail(val error: String) : Resource<Nothing>()
    data class Error(val exception: Exception) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[result=$data]"
            is Fail -> "Fail[error=$error]"
            is Error -> "Error[exception=${exception.message}]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Resource] is of catalogType [Success] & holds non-null [Success.data].
 */
val Resource<*>.succeeded
    get() = this is Resource.Success && data != null
