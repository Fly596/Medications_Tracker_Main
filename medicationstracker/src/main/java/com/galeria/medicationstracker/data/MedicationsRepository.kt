package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService.db
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface MedicationsRepository {
  
  suspend fun getDrug(): UserMedication
  suspend fun getDrugs(uid: String): List<UserMedication>
  suspend fun deleteDrug(drugName: String)
  
  suspend fun addDrug(drug: UserMedication)
  
  suspend fun updateDrug(
    endDate: Timestamp,
    startDate: Timestamp,
    medName: String,
    medForm: String,
    selectedDays: List<String>,
    intakeTime: String,
    notes: String,
    strength: Float,
    strengthUnit: String,
    uid: String
  )
  
  fun getDrugsStream(uid: String): Flow<List<UserMedication>>
  
}

class MedicationsRepositoryImpl @Inject constructor(
  private val firestore: FirebaseFirestore
) : MedicationsRepository {
  
  private val medicationsCollection = firestore.collection("UserMedication")
  
  override fun getDrugsStream(uid: String): Flow<List<UserMedication>> {
    return callbackFlow {
      val listenerRegistration = medicationsCollection
        .whereEqualTo("uid", uid)
        .addSnapshotListener { value, error ->
          if (error != null) {
            // Handle error
            return@addSnapshotListener
          }
          
          if (value != null) {
            val medsList = value.toObjects(UserMedication::class.java)
            trySend(medsList)
          }
        }
      // Clean up the listener when the flow is cancelled
      awaitClose {
        listenerRegistration.remove()
      }
    }
  }
  
  override suspend fun getDrug(): UserMedication {
    TODO("Not yet implemented")
  }
  
  override suspend fun getDrugs(uid: String): List<UserMedication> {
    return try {
      val querySnapshot = db.collection("UserMedication")
        .whereEqualTo("uid", uid)
        .get()
        .await()
      querySnapshot.toObjects(UserMedication::class.java)
    } catch (e: Exception) {
      emptyList()
    }
    
  }
  
  override suspend fun addDrug(drug: UserMedication) {
    TODO("Not yet implemented")
  }
  
  override suspend fun updateDrug(
    endDate: Timestamp,
    startDate: Timestamp,
    medName: String,
    medForm: String,
    selectedDays: List<String>,
    intakeTime: String,
    notes: String,
    strength: Float,
    strengthUnit: String,
    uid: String
  ) {
    TODO("Not yet implemented")
  }
  
  override suspend fun deleteDrug(drugName: String) {
    medicationsCollection
      .whereEqualTo("name", drugName)
      .get()
      .addOnSuccessListener { querySnapshot ->
        if (!querySnapshot.isEmpty) {
          for (document in querySnapshot.documents) {
            db.collection("UserMedication")
              .document(document.id)
              .delete()
              .addOnSuccessListener {
                println("Document with ID ${document.id} successfully deleted!")
              }
              .addOnFailureListener { e ->
                println("Error deleting document with ID ${document.id}: $e")
              }
          }
        } else {
          println("No document found with the name: ${drugName}")
        }
      }
      .addOnFailureListener { e ->
        println("Error finding documents to delete: $e")
      }
  }
  
}