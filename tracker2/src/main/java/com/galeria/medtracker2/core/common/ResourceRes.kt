package com.galeria.medtracker2.core.common

sealed interface ResourceRes<out T> {

    data class Success<T>(val data: T) : ResourceRes<T>

    data class Error(val message: String) : ResourceRes<Nothing>

    data object Loading : ResourceRes<Nothing>
}