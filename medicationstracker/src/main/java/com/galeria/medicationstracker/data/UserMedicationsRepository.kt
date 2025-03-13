package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.utils.FirestoreFunctions
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService.db
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserMedicationsRepository {
    
    suspend fun getDrug(drugName: String): UserMedication

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
        uid: String,
    )

    fun getDrugsStream(uid: String): Flow<List<UserMedication>>
}

class UserMedicationsRepositoryImpl
@Inject
constructor(private val firestore: FirebaseFirestore, private val auth: FirebaseAuth) :
    UserMedicationsRepository {
    
    private val userMedicationsCollection = firestore.collection("UserMedication")
    
    override fun getDrugsStream(uid: String): Flow<List<UserMedication>> = callbackFlow {
        val listenerRegistration =
            userMedicationsCollection.whereEqualTo("uid", uid).addSnapshotListener { value, error ->
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
        awaitClose { listenerRegistration.remove() }
    }
    
    override suspend fun getDrug(drugName: String): UserMedication {
        return try {
            val querySnapshot =
                db.collection("UserMedication")
                    .whereEqualTo("name", drugName)
                    .whereEqualTo("uid", auth.currentUser?.uid)
                    .get()
                    .await()
            querySnapshot.toObjects(UserMedication::class.java)[0]
        } catch (e: Exception) {
            UserMedication()
        }
    }
    
    override suspend fun getDrugs(uid: String): List<UserMedication> {
        return try {
            val querySnapshot =
                db.collection("UserMedication").whereEqualTo("uid", uid).get().await()
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
        uid: String,
    ) {
        val newValues: Map<String, Any?> =
            mapOf(
                "endDate" to endDate,
                "form" to medForm,
                "daysOfWeek" to selectedDays,
                "intakeTime" to intakeTime,
                "name" to medName,
                "notes" to notes,
                "strength" to strength,
                "unit" to strengthUnit,
                "startDate" to startDate,
                "uid" to uid,
            )
        val medicationRef =
            db.collection(
                "UserMedication"
            )
        medicationRef
            .whereEqualTo("uid", uid)
            .whereEqualTo("name", medName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                querySnapshot.toObjects(UserMedication::class.java)[0]
                val documentId = querySnapshot.documents[0].id
                medicationRef.document(documentId).update(newValues)
            }
            .addOnFailureListener { exception ->
                // Toast.makeText(context, "Error updating medication", Toast.LENGTH_SHORT).show()
            }
    }
    
    override suspend fun deleteDrug(drugName: String) {
        userMedicationsCollection
            .whereEqualTo("name", drugName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        FirestoreFunctions.FirestoreService.db
                            .collection("UserMedication")
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
            .addOnFailureListener { e -> println("Error finding documents to delete: $e") }
    }
}
