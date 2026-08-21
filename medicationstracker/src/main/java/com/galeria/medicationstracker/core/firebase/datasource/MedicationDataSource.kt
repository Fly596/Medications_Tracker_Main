package com.galeria.medicationstracker.core.firebase.datasource

import com.galeria.medicationstracker.core.firebase.model.MedicationDocument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface MedicationDataSource {

  suspend fun addMedication(medication: MedicationDocument): String

  suspend fun getMedication(medicationId: String): MedicationDocument?

  suspend fun getMedications(): List<MedicationDocument>

  suspend fun updateMedication(medication: MedicationDocument)

  fun getMedicationsFlow(): Flow<List<MedicationDocument>>

  suspend fun deleteMedication(medicationId: String)
}

class MedicationDataSourceImpl @Inject constructor(
  private val auth: FirebaseAuth,
  private val firestore: FirebaseFirestore
) : MedicationDataSource {

  companion object {

    private const val USERS_COLLECTION = "User"
    private const val MEDICATIONS_SUBCOLLECTION = "medications"
  }

  // 1. Единая точка получения userId
  private val currentUserId: String
    get() = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")

  // 2. Единая точка доступа к коллекции пользователя
  private val userMedicationsCollection: CollectionReference
    get() = firestore.collection(USERS_COLLECTION)
      .document(currentUserId)
      .collection(MEDICATIONS_SUBCOLLECTION)

  override suspend fun addMedication(medication: MedicationDocument): String {
    val dataToSave = medication.copy(id = "")

    return try {
      userMedicationsCollection
        .add(dataToSave)
        .await().id
    } catch (e: Exception) {
      throw e
    }
  }

  override suspend fun getMedication(
    medicationId: String
  ): MedicationDocument? {
    val snapshot = userMedicationsCollection.document(medicationId).get().await()
    return if (snapshot.exists()) snapshot.toObject(MedicationDocument::class.java) else null
  }

  override suspend fun getMedications(): List<MedicationDocument> {
    return userMedicationsCollection.get().await().toObjects(MedicationDocument::class.java)
  }

  override suspend fun updateMedication(medication: MedicationDocument) {
    TODO("Not yet implemented")
  }

  override fun getMedicationsFlow(): Flow<List<MedicationDocument>> =
      callbackFlow {
        // Слушатель привязывается к коллекции текущего пользователя
        val listenerRegistration = userMedicationsCollection
          .addSnapshotListener { snapshot, error ->
            if (error != null) {
              close(error)
              return@addSnapshotListener
            }
            if (snapshot != null) {
              val medications = snapshot.toObjects(MedicationDocument::class.java)
              trySend(medications)
            }
          }
        awaitClose { listenerRegistration.remove() }
      }

  override suspend fun deleteMedication(medicationId: String) {
    TODO("Not yet implemented")
  }
}