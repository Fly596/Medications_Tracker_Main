package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.data.network.NetworkUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface NewUserRepository {
    
    suspend fun addUser(networkUser: NetworkUser): Result<String>
    
    suspend fun getUserData(userId: String): Result<NetworkUser>
    
    suspend fun updateUser(networkUser: NetworkUser): Result<Unit>
}

@Singleton
class NewUserRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    NewUserRepository {
    
    companion object {
        
        private const val USERS_COLLECTION = "User"
    }
    
    override suspend fun addUser(networkUser: NetworkUser): Result<String> {
        val dataToSave = networkUser.copy(id = "")
        
        return try {
            firestore.collection(USERS_COLLECTION).document(networkUser.id)
                .set(dataToSave).await()
            Result.success(dataToSave.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun getUserData(userId: String): Result<NetworkUser> {
        return try {
            val documentSnapshot =
                firestore.collection(USERS_COLLECTION).document(userId).get()
                    .await()
            if (documentSnapshot.exists()) {
                val networkUser =
                    documentSnapshot.toObject(NetworkUser::class.java)
                if (networkUser != null) {
                    Result.success(networkUser)
                } else {
                    Result.failure(Exception("Failed to parse user data for ID: $userId"))
                }
            } else {
                Result.failure(Exception("User not found with ID: $userId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun updateUser(networkUser: NetworkUser): Result<Unit> {
        TODO("Not yet implemented")
    }
}
