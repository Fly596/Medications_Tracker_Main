package com.galeria.medicationstracker.feature_auth.domain.repository

import com.galeria.medicationstracker.utils.ResourceRes
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun login(email: String, password: String): ResourceRes<FirebaseUser>

    suspend fun register(email: String, password: String): ResourceRes<FirebaseUser>

    suspend fun restorePassword(email: String)

    fun getAuthState(): Flow<FirebaseUser?>

    suspend fun signOut()
}
