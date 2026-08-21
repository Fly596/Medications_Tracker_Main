package com.galeria.medicationstracker.core.firebase.datasource

import com.galeria.medicationstracker.core.firebase.model.UserDocument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserDataSource {

  suspend fun addUser(user: UserDocument)

  suspend fun getUserData(userUid: String = ""): UserDocument?

  fun getUserFlow(): Flow<UserDocument?>

  suspend fun updateUser(user: UserDocument)
}

class UserDataSourceImpl @Inject constructor(
  private val firestore: FirebaseFirestore,
  private val auth: FirebaseAuth,
) : UserDataSource {

  companion object {

    private const val USERS_COLLECTION = "User"
  }

  // 1. Единая точка получения userId.
  private val currentUserId: String
    get() = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

  // 2. Единая точка доступа к коллекции пользователя.
  private val documentReference: DocumentReference
    get() = firestore.collection(USERS_COLLECTION)
      .document(currentUserId)

  override suspend fun addUser(user: UserDocument) {
    documentReference
      .set(user)
      .await()
  }

  override suspend fun getUserData(userUid: String): UserDocument? {
    // Если не указан userId, то берем текущего пользователя.
    if (userUid.isEmpty()) {
      return getUserData(currentUserId)
    }

    // Если указан userId, то берем данные из Firestore.
    val snapshot = firestore.collection(USERS_COLLECTION)
      .document(userUid).get().await()
    return snapshot.toObject(UserDocument::class.java)
  }

  override fun getUserFlow(): Flow<UserDocument?> = callbackFlow {
    val listener = documentReference
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error); return@addSnapshotListener
        }
        trySend(snapshot?.toObject(UserDocument::class.java))
      }
    awaitClose { listener.remove() }
  }

  override suspend fun updateUser(user: UserDocument) {
    documentReference
      .set(user)
      .await()
  }
}