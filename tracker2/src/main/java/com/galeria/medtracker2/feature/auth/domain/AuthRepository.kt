package com.galeria.medtracker2.feature.auth.domain

import com.galeria.medtracker2.core.common.ResultState
import com.galeria.medtracker2.feature.auth.domain.model.UserDomain
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    
    suspend fun login(
        email: String,
        password: String
    ): ResultState<FirebaseUser>
    
    suspend fun register(
        email: String,
        password: String
    ): ResultState<FirebaseUser>
    
    suspend fun restorePassword(email: String): Boolean
    
    suspend fun signOut()
    
    suspend fun addUser(userDomain: UserDomain): ResultState<String>
}