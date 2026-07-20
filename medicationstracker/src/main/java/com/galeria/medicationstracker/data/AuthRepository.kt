package com.galeria.medicationstracker.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Result<Unit>

    suspend fun signUp(email: String, password: String): Result<Unit>

    suspend fun resetPassword(email: String): Result<Unit>

    suspend fun getUserId(): Result<String?>

    suspend fun getUserEmail(): Result<String?>
}

@Singleton
class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) : AuthRepository {

    override suspend fun getUserId(): Result<String?> {
        return runCatching { auth.currentUser?.uid }
    }

    override suspend fun getUserEmail(): Result<String?> {
        return runCatching { auth.currentUser?.email }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(Exception("User not found."))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(Exception("Invalid email or password."))
        } catch (e: Exception) {
            Result.failure(Exception("Auth failed: ${e.message}"))
        }
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Auth failed: ${e.message}"))
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return suspendCoroutine { continuation ->
            auth
                .sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        continuation.resume(Result.success(Unit))
                    } else {
                        continuation.resume(
                            Result.failure(
                                task.exception ?: RuntimeException("Password reset failed")
                            )
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resume(Result.failure(exception))
                }
        }
    }
}
