package com.galeria.medtracker2.utils

sealed interface ResourceRes<out T> {

    data class Success<T>(val data: T) : ResourceRes<T>

    data class Error(val message: String) : ResourceRes<Nothing>

    data object Loading : ResourceRes<Nothing>
}
