package com.galeria.medicationstracker.feature_auth.data.repository

import com.galeria.medicationstracker.feature_auth.data.source.local.UserDao
import com.galeria.medicationstracker.feature_auth.domain.model.UserDomain
import com.galeria.medicationstracker.feature_auth.domain.repository.AuthRepository
import com.galeria.medicationstracker.feature_auth.utils.toDto
import com.galeria.medicationstracker.feature_auth.utils.toEntity
import com.galeria.medicationstracker.utils.Response
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
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Response<FirebaseUser> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()

            if (authResult.user != null) {
                Response.Success(authResult.user!!)
            } else {
                Response.Error("User Data is null")
            }
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    override suspend fun register(email: String, password: String): Response<FirebaseUser> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user != null) {
                Response.Success(user)
            } else {
                Response.Error("Registration failed: User is null.")
            }
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "Registration error")
        }
    }

    override suspend fun addUser(userDomain: UserDomain): Response<String> {
        return try {
            // Подготовка данных.
            val userDto = userDomain.toDto()
            val userEntity = userDomain.toEntity()
            userDao.insertUser(userEntity)

            firestore.collection("User").document(userDomain.id).set(userDto).await()

            Response.Success("User saved successfully")
        } catch (e: Exception) {
            Response.Error(e.localizedMessage ?: "Failed to save user data")
        }
    }

    override suspend fun restorePassword(email: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun signOut() {
        TODO("Not yet implemented")
    }
}
