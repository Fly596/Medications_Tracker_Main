package com.galeria.medicationstracker.core.firebase.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface AuthDataSource {

  val authStateFlow: Flow<FirebaseUser?>

  suspend fun signIn(email: String, password: String): FirebaseUser

  suspend fun signUp(email: String, password: String): FirebaseUser

  suspend fun resetPassword(email: String)
}

class AuthDataSourceImpl
@Inject constructor(private val auth: FirebaseAuth) : AuthDataSource {

  override val authStateFlow: Flow<FirebaseUser?> = callbackFlow {
    val listener = FirebaseAuth.AuthStateListener { auth ->
      trySend(auth.currentUser)
    }

    auth.addAuthStateListener(listener)

    awaitClose { auth.removeAuthStateListener(listener) }
  }

  override suspend fun signIn(email: String, password: String): FirebaseUser {
    val result = auth.signInWithEmailAndPassword(email, password).await()
    return result.user ?: throw IllegalStateException("FirebaseUser is null after login")
  }

  override suspend fun signUp(email: String, password: String): FirebaseUser {
    val result = auth.createUserWithEmailAndPassword(email, password).await()
    return result.user ?: throw IllegalStateException("FirebaseUser is null after signup")
  }

  override suspend fun resetPassword(email: String) {
    auth.sendPasswordResetEmail(email).await()
  }
}
