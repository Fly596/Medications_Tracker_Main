package com.galeria.medtracker2.feature_auth.data.repository

import com.galeria.medtracker2.feature_auth.data.source.local.UserDao
import com.galeria.medtracker2.feature_auth.data.toDto
import com.galeria.medtracker2.feature_auth.data.toEntity
import com.galeria.medtracker2.feature_auth.domain.UserDomain
import com.galeria.medtracker2.feature_auth.domain.repository.AuthRepository
import com.galeria.medtracker2.utils.ResourceRes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

// TODO: 12/1/2025, 1:35pm
class AuthRepositoryImpl
@Inject
constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val userDao: UserDao,
) : AuthRepository {

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
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                ResourceRes.Success(user)
            } else {
                ResourceRes.Error("Registration failed: User is null.")
            }
        } catch (e: Exception) {
            ResourceRes.Error(e.localizedMessage ?: "Registration error")
        }
    }

    override suspend fun addUser(userDomain: UserDomain): ResourceRes<String> {
        return try {
            // Подготовка данных.
            val userDto = userDomain.toDto()
            val userEntity = userDomain.toEntity()

            firestore.collection("users").document(userDomain.id).set(userDto).await()

            userDao.insertUser(userEntity)

            ResourceRes.Success("User saved successfully")
        } catch (e: Exception) {
            ResourceRes.Error(e.localizedMessage ?: "Failed to save user data")
        }
    }

    override suspend fun restorePassword(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}
