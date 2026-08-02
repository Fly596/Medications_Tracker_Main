package com.galeria.medicationstracker.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface NewUserRepository {

  suspend fun addUser(user: NewUser): Result<String>

  suspend fun getUserData(userId: String): Result<NewUser>

  suspend fun updateUser(user: NewUser): Result<Unit>
}

@Singleton
class NewUserRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
  NewUserRepository {

  companion object {

    private const val USERS_COLLECTION = "User"
  }

  override suspend fun addUser(user: NewUser): Result<String> {
    val dataToSave = user.copy(id = "")

    return try {
      firestore.collection(USERS_COLLECTION).document(user.id)
        .set(dataToSave).await()
      Result.success(dataToSave.id)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }

  override suspend fun getUserData(userId: String): Result<NewUser> {
    return try {
      val documentSnapshot =
          firestore.collection(USERS_COLLECTION).document(userId).get().await()
      if (documentSnapshot.exists()) {
        val user = documentSnapshot.toObject(NewUser::class.java)
        if (user != null) {
          Result.success(user)
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

  override suspend fun updateUser(user: NewUser): Result<Unit> {
    return try {
      firestore.collection(USERS_COLLECTION).document(user.id).set(user).await()
      Result.success(Unit)
    } catch (e: Exception) {
      Result.failure(e)
    }
  }
}
