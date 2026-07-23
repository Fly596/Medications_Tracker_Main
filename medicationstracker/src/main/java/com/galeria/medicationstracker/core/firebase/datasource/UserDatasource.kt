package com.galeria.medicationstracker.core.firebase.datasource

import com.galeria.medicationstracker.core.firebase.model.UserDocument
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserDatasource {

  suspend fun addUser(user: UserDocument)

  suspend fun getUserData(userId: String): UserDocument?

  fun getUserFlow(userId: String): Flow<UserDocument?>

  suspend fun updateUser(user: UserDocument)
}

class UserDataSourceImpl @Inject constructor(
  private val firestore: FirebaseFirestore
) : UserDatasource {

  override suspend fun addUser(user: UserDocument) {
    firestore.collection("User")
      .document(user.id)
      .set(user)
      .await()
  }

  override suspend fun getUserData(userId: String): UserDocument? {
    val snapshot = firestore.collection("User").document(userId).get().await()
    return snapshot.toObject(UserDocument::class.java)
  }

  override fun getUserFlow(userId: String): Flow<UserDocument?> = callbackFlow {
    val listener = firestore.collection("User").document(userId)
      .addSnapshotListener { snapshot, error ->
        if (error != null) {
          close(error); return@addSnapshotListener
        }
        trySend(snapshot?.toObject(UserDocument::class.java))
      }
    awaitClose { listener.remove() }
  }

  override suspend fun updateUser(user: UserDocument) {
    firestore.collection("User")
      .document(user.id)
      .set(user)
      .await()
  }
}