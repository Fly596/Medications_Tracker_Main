package com.galeria.medicationstracker.feature_auth.data.repository

import com.galeria.medicationstracker.feature_auth.domain.repository.AuthRepository
import com.galeria.medicationstracker.utils.ResourceRes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// TODO: 12/1/2025, 1:35pm
class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {

    override suspend fun login(email: String, password: String): ResourceRes<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            if (authResult.user != null) {
                ResourceRes.Success(authResult.user!!)
            } else {
                ResourceRes.Error("User Data is null")
            }
        } catch (e: Exception) {
            ResourceRes.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun register(email: String, password: String): ResourceRes<FirebaseUser> {
        TODO("Not yet implemented")
    }

    override suspend fun restorePassword(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}
