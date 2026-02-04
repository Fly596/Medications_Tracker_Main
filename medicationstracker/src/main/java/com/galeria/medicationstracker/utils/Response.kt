package com.galeria.medicationstracker.utils

sealed interface Response<out T> {

    data class Success<T>(val data: T) : Response<T>

    data class Error(val message: String) : Response<Nothing>

    data object Loading : Response<Nothing>
}