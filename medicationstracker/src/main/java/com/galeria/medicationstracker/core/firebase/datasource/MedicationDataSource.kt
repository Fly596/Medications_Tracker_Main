package com.galeria.medicationstracker.core.firebase.datasource

import com.galeria.medicationstracker.core.firebase.model.MedicationDocument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface MedicationDataSource {

  suspend fun addMedication(userId: String, medication: MedicationDocument): String

  suspend fun getMedication(userId: String, medicationId: String): MedicationDocument?

  suspend fun getMedications(userId: String): List<MedicationDocument>

  suspend fun updateMedication(userId: String, medication: MedicationDocument)

  fun getMedicationsFlow(userId: String): Flow<List<MedicationDocument>>

  suspend fun deleteMedication(userId: String, medicationId: String)
}

class MedicationDataSourceImpl @Inject constructor(
  private val auth: FirebaseAuth,
  private val firestore: FirebaseFirestore
) : MedicationDataSource {

  companion object {

    private const val USERS_COLLECTION = "User"
    private const val MEDICATIONS_SUBCOLLECTION = "medications"
  }

  override suspend fun addMedication(userId: String, medication: MedicationDocument): String {
    val dataToSave = medication.copy(userId = userId, id = "")

    return try {
      firestore.collection(USERS_COLLECTION).document(userId)
        .collection(MEDICATIONS_SUBCOLLECTION)
        .add(dataToSave)
        .await().id
    } catch (e: Exception) {
      throw e
    }
  }

  override suspend fun getMedication(
    userId: String,
    medicationId: String
  ): MedicationDocument? {
    return try {
      val documentSnapshot =
          firestore.collection(USERS_COLLECTION).document(userId)
            .collection(MEDICATIONS_SUBCOLLECTION)
            .document(medicationId)
            .get().await()
      if (documentSnapshot.exists()) {
        documentSnapshot.toObject(MedicationDocument::class.java)
      } else {
        null
      }
    } catch (e: Exception) {
      throw e
    }
  }

  override suspend fun getMedications(userId: String): List<MedicationDocument> {
    return try {
      firestore.collection(USERS_COLLECTION).document(userId).collection(MEDICATIONS_SUBCOLLECTION)
        .get().await().toObjects(MedicationDocument::class.java)
    } catch (e: Exception) {
      throw e
    }
  }

  override suspend fun updateMedication(userId: String, medication: MedicationDocument) {
    TODO("Not yet implemented")
  }

  override fun getMedicationsFlow(userId: String): Flow<List<MedicationDocument>> =
      callbackFlow {
        val listenerRegistration =
            firestore.collection(USERS_COLLECTION).document(userId)
              .collection(MEDICATIONS_SUBCOLLECTION)
              .addSnapshotListener { snapshot, error ->
                if (error != null) {
                  close(error)
                  return@addSnapshotListener
                }
                if (snapshot != null) {
                  val medications = snapshot.toObjects(MedicationDocument::class.java)
                  trySend(medications).isSuccess
                }
              }
        awaitClose { listenerRegistration.remove() }
      }

  override suspend fun deleteMedication(userId: String, medicationId: String) {
    TODO("Not yet implemented")
  }
}