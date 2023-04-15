package com.james.submissiononefundamentalandroiddicoding

sealed class Result<out R> private constructor() {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error<out T>(val error: String) : Result<T>()
    object Loading : Result<Nothing>()
}