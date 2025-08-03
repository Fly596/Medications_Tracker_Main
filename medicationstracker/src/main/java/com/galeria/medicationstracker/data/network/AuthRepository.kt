package com.galeria.medicationstracker.data.network

import com.galeria.medicationstracker.utils.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

sealed interface AuthError {
    data object WrongPassword : AuthError
    data object UserNotFound : AuthError
    data object WeakPassword : AuthError
    data object InvalidEmail : AuthError
    data object UserCollision : AuthError
    data class Unknown(val message: String?) : AuthError
}

interface AuthRepository {
    
    fun getAuthState(): Flow<FirebaseUser?>
    
    suspend fun signIn(
        email: String,
        password: String
    ): AuthResult
    
    suspend fun signUp(
        email: String,
        password: String
    ): AuthResult
    
    suspend fun resetPassword(email: String): AuthResult
    
    suspend fun signOut()
    
    suspend fun getUserId(): Result<String?>
    
    suspend fun getUserEmail(): Result<String?>
}

@Singleton
class AuthRepositoryImpl @Inject constructor(private val auth: FirebaseAuth) :
    AuthRepository {
    
    override fun getAuthState(): Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
    
    override suspend fun getUserId(): Result<String?> {
        return Result.success(auth.currentUser?.uid)
        // return runCatching { auth.currentUser?.uid }
    }
    
    override suspend fun getUserEmail(): Result<String?> {
        return Result.success(auth.currentUser?.email)
    }
    
    override suspend fun signIn(
        email: String,
        password: String
    ): AuthResult {
        if (email.isBlank() || password.isBlank()) {
            return AuthResult.ValidationError("Blank fields")
        }
        runAuthOperation {
            auth.signInWithEmailAndPassword(email, password).await()
        }
        return AuthResult.Success /* Result.success("ass") */
    }
    /*         return runAuthOperation {
                auth.signInWithEmailAndPassword(email, password).await()
            } */
    // return runAuth
    /*         return try {
                auth.signInWithEmailAndPassword(email, password).await()
                AuthResult.Success
            } catch (e: FirebaseAuthInvalidUserException) {
                AuthResult.AuthError("User Not Found")
                // AuthResult.failure(Exception("User not found."))
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                AuthResult.AuthError("Invalid email or password.")
                // Result.failure(Exception("Invalid email or password."))
            } catch (e: IOException) {
                AuthResult.NetworkError
                // Result.failure(Exception("Auth failed: ${e.message}"))
            } catch (e: Exception) {
                AuthResult.UnknownError("${e.message}")
                // Result.failure(Exception("Auth failed: ${e.message}"))
            } */
    override suspend fun signUp(email: String, password: String): AuthResult {
        if (email.isBlank() || password.isBlank()) {
            return AuthResult.ValidationError("Email or password cannot be empty.")
        }
        
        return runAuthOperation {
            auth.createUserWithEmailAndPassword(email, password).await()
        }
        /*  return try {
             auth.createUserWithEmailAndPassword(email, password).await()
             AuthResult.Success
         } catch (e: FirebaseAuthUserCollisionException) {
             AuthResult.AuthError("User already exists.")
         } catch (e: IOException) {
             AuthResult.NetworkError
         } catch (e: Exception) {
             AuthResult.UnknownError("${e.message}")
         } */
    }
    
    // Сбрасывает пароль.
    override suspend fun resetPassword(email: String): AuthResult {
        return runAuthOperation {
            auth.sendPasswordResetEmail(email).await()
        }
        /*         return suspendCoroutine { continuation ->
                    auth
                        .sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                continuation.resume(Result.success(Unit))
                            } else {
                                continuation.resume(
                                    Result.failure(
                                        task.exception
                                            ?: RuntimeException("Password reset failed")
                                    )
                                )
                            }
                        }
                        .addOnFailureListener { exception ->
                            continuation.resume(Result.failure(exception))
                        }
                } */
    }
    
    // Выход из аккаунта.
    override suspend fun signOut() {
        withContext(Dispatchers.IO) {
            auth.signOut()
        }
    }
    
    private suspend fun <T> runAuthOperation(block: suspend () -> T): AuthResult {
        return try {
            block()
            AuthResult.Success
        } catch (e: FirebaseAuthInvalidUserException) {
            AuthResult.AuthError("User not found.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            AuthResult.AuthError("Invalid email or password.")
        } catch (e: IOException) {
            AuthResult.NetworkError
        } catch (e: Exception) {
            AuthResult.UnknownError("${e.message}")
        }
    }
}
