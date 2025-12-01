package com.galeria.medicationstracker.feature_auth.domain.repository

import com.galeria.medicationstracker.feature_auth.domain.UserDomain
import com.galeria.medicationstracker.utils.ResourceRes
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun login(email: String, password: String): ResourceRes<FirebaseUser>

    suspend fun register(email: String, password: String): ResourceRes<FirebaseUser>

    suspend fun restorePassword(email: String): Boolean

    suspend fun signOut()

    suspend fun addUser(userDomain: UserDomain): ResourceRes<String>
}
