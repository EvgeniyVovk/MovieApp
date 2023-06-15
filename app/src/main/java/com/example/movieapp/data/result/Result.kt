package com.example.movieapp.data.result

sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class ConnectionError(val message: String?, val error: String? = null) : Result<Nothing>()
    data class ServerError(val message: String?, val error: String? = null) : Result<Nothing>()
    data class EnqueueError(val message: String?, val error: String? = null) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is ConnectionError -> "ConnectionError[message=$message, exception=$error]"
            is ServerError -> "ServerError[message=$message, exception=$error]"
            is EnqueueError -> "EnqueueError[message=$message, exception=$error]"
        }
    }
}