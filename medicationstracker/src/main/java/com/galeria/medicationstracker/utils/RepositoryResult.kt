package com.galeria.medicationstracker.utils

sealed class RepositoryResult<out S, out E> {
    data class Success<out S>(val success: S) : RepositoryResult<S, Nothing>()
    data class Error<out E>(val error: E) : RepositoryResult<Nothing, E>()
    
}

sealed class AuthResult {
    object Success : AuthResult()
    data class AuthError(val message: String) : AuthResult()
    data class ValidationError(val message: String) : AuthResult()
    object NetworkError : AuthResult()
    data class UnknownError(val message: String) : AuthResult()
}