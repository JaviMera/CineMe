package com.merajavier.cineme.common

sealed class TMDBApiResult<out T: Any> {
    data class Success<out T: Any>(val data: T) : TMDBApiResult<T>()
    data class Failure<out T: Any>(val data: T) : TMDBApiResult<T>()
    data class Error(val message: String?, val statusCode: Int? = null) : TMDBApiResult<Nothing>()
    object Init : TMDBApiResult<Nothing>()
}