package com.galeria.medicationstracker.auth.domain.repository

interface AuthRepository {
    
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(
        email: String,
        password: String,
        firstName: String,
        lastName: String
    )
    
    suspend fun resetPassword(email: String)
    suspend fun signOut()
}