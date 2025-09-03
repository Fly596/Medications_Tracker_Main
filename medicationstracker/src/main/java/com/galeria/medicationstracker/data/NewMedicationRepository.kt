package com.galeria.medicationstracker.data

import com.galeria.medicationstracker.data.local.MedicationDao
import com.galeria.medicationstracker.data.network.NetworkMedication
import com.galeria.medicationstracker.utils.formatTimestampToWeekday
import com.galeria.medicationstracker.utils.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

interface NewMedicationRepository {

    fun getUserMedications(userId: String): Flow<List<NetworkMedication>>
    
    suspend fun getMedication(
        userId: String,
        medicationId: String
    ): Result<NetworkMedication>
    
    suspend fun addMedication(networkMedication: NetworkMedication): String?

    suspend fun updateMedication(medication: NetworkMedication): Result<Unit>
    
    suspend fun deleteMedication(
        userId: String,
        medicationId: String
    ): Result<Unit>

    fun getTodaysMedications(userId: String): Flow<List<NetworkMedication>>
}

@Singleton
class NewMedicationRepositoryImpl
@Inject
constructor(
    private val firestore: FirebaseFirestore,
    private val medicationDao: MedicationDao
) :
    NewMedicationRepository {

    companion object {

        private const val USERS_COLLECTION = "User"
        private const val MEDICATIONS_SUBCOLLECTION = "medications"
    }
    
    override fun getUserMedications(userId: String): Flow<List<NetworkMedication>> =
        callbackFlow {
            val listenerRegistration =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(MEDICATIONS_SUBCOLLECTION)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val medications =
                                snapshot.toObjects(NetworkMedication::class.java)
                            trySend(medications).isSuccess
                        }
                    }
            awaitClose { listenerRegistration.remove() }
        }

    override suspend fun getMedication(
        userId: String,
        medicationId: String,
    ): Result<NetworkMedication> {
        return try {
            val documentSnapshot =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(MEDICATIONS_SUBCOLLECTION)
                    .document(medicationId)
                    .get()
                    .await()
            if (documentSnapshot.exists()) {
                val medication =
                    documentSnapshot.toObject(NetworkMedication::class.java)
                if (medication != null) {
                    Result.success(medication)
                } else {
                    Result.failure(
                        Exception("Failed to parse medication data for ID: $medicationId")
                    )
                }
            } else {
                Result.failure(
                    Exception("Medication not found with ID: $medicationId for user: $userId")
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun addMedication(networkMedication: NetworkMedication): String? {
        return suspendCancellableCoroutine { continuation ->
            firestore
                .collection(USERS_COLLECTION)
                .document(networkMedication.userId)
                .collection(MEDICATIONS_SUBCOLLECTION)
                .add(networkMedication)
                .addOnSuccessListener { documentReference ->
                    val firestoreId = documentReference.id
                    println("DocumentSnapshot added with ID: $firestoreId")
                    val medicationEntity =
                        networkMedication.toEntity(
                            firestoreId = firestoreId,
                        )
                    
                    GlobalScope.launch(Dispatchers.IO) {
                        medicationDao.upsertMedication(medicationEntity)
                        println("MedicationEntity upserted with ID: $firestoreId")
                        
                        if (continuation.isActive) {
                            continuation.resume(firestoreId) { cause, _, _ ->
                                null?.let { it(cause) }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    // --- ОШИБКА В FIRESTORE ---
                    println("Firestore: Ошибка создания документа: $e")
                    if (continuation.isActive) {
                        continuation.resume(null) // Возвращаем null в случае ошибки
                    }
                }
        }
    }

    override suspend fun updateMedication(medication: NetworkMedication): Result<Unit> {
        if (medication.id.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Medication ID cannot be blank for update.")
            )
        }
        // val dataToSave = medication.copy(userId = userId)

        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(medication.userId)
                .collection(MEDICATIONS_SUBCOLLECTION)
                .document(medication.id)
                .set(medication)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteMedication(
        userId: String,
        medicationId: String
    ): Result<Unit> {
        if (medicationId.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Medication ID cannot be blank for delete.")
            )
        }
        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(MEDICATIONS_SUBCOLLECTION)
                .document(medicationId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun getTodaysMedications(userId: String): Flow<List<NetworkMedication>> =
        callbackFlow {
            val todayEnd =
                LocalDate.now().plusDays(1).atStartOfDay().toTimestamp()
            val todayWeekDay =
                formatTimestampToWeekday(Timestamp.now()).uppercase()
            val listenerRegistration =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(MEDICATIONS_SUBCOLLECTION)
                    .whereGreaterThanOrEqualTo("endDate", todayEnd)
                    .whereArrayContains("daysOfWeek", todayWeekDay)
                    .addSnapshotListener { snapshot, error ->
                        if (error != null) {
                            close(error)
                            return@addSnapshotListener
                        }
                        if (snapshot != null) {
                            val medications =
                                snapshot.toObjects(NetworkMedication::class.java)
                            trySend(medications).isSuccess
                        }
                    }
            awaitClose { listenerRegistration.remove() }
        }
}
