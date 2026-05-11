package com.example.samba.domain.model

sealed class SmbFileResult<out T> {

    data class Success<T>(val data: T) : SmbFileResult<T>()
    data class Error(
        val message: String,
        val cause: Throwable? = null
    ) : SmbFileResult<Nothing>()
}
