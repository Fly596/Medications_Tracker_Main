package com.galeria.medicationstracker.feature_auth.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String)

    suspend fun register(email: String, password: String)

    suspend fun restorePassword(email: String)
}
