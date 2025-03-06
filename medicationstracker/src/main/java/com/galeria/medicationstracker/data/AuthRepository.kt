package com.galeria.medicationstracker.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthRepository {
  
  suspend fun register(email: String, password: String): Result<FirebaseUser>
  
  suspend fun signIn(email: String, password: String): Result<FirebaseUser>
  
  suspend fun resetPassword(email: String): Result<Unit>
  
  suspend fun logout()
  
  suspend fun getCurrentUserId(): String?
}

class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) :
  AuthRepository {
  
  override suspend fun register(
    email: String,
    password: String
  ): Result<FirebaseUser> {
    return try {
      val authResult =
        auth.createUserWithEmailAndPassword(email, password).await()
      val user: FirebaseUser? = authResult.user
      
      if (user == null) {
        Result.failure(IllegalStateException("User is null after successful sign-up"))
      } else {
        Result.success(user)
      }
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
  
  override suspend fun signIn(
    email: String,
    password: String
  ): Result<FirebaseUser> {
    return try {
      val result =
        auth.signInWithEmailAndPassword(email, password).await()
      val user = result.user
        ?: return Result.failure(NullPointerException("User is null"))
      Result.success(user)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
  
  override suspend fun resetPassword(email: String): Result<Unit> {
    return try {
      auth.sendPasswordResetEmail(email).await()
      Result.success(Unit)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
  
  override suspend fun logout() {
    auth.signOut()
  }
  
  override suspend fun getCurrentUserId(): String? {
    return auth.currentUser?.uid
  }
}
