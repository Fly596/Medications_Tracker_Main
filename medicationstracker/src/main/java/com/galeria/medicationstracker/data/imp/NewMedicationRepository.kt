package com.galeria.medicationstracker.data.imp

import com.galeria.medicationstracker.utils.formatTimestampToWeekday
import com.galeria.medicationstracker.utils.toTimestamp
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface NewMedicationRepository {

    fun observeUserMedications(userId: String): Flow<List<NewUserMedication>>

    suspend fun getMedication(userId: String, medicationId: String): Result<NewUserMedication>

    suspend fun addMedication(userId: String, medicationData: NewUserMedication): Result<String>

    suspend fun updateMedication(userId: String, medication: NewUserMedication): Result<Unit>

    suspend fun deleteMedication(userId: String, medicationId: String): Result<Unit>

    fun getTodaysIntakes(userId: String): Flow<List<NewUserMedication>>
}

@Singleton
class NewMedicationRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    NewMedicationRepository {

    companion object {

        private const val USERS_COLLECTION = "User"
        private const val MEDICATIONS_SUBCOLLECTION = "medications"
    }

    override fun observeUserMedications(userId: String): Flow<List<NewUserMedication>> =
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
                            val medications = snapshot.toObjects(NewUserMedication::class.java)
                            trySend(medications).isSuccess
                        }
                    }
            awaitClose { listenerRegistration.remove() }
        }

    override suspend fun getMedication(
        userId: String,
        medicationId: String,
    ): Result<NewUserMedication> {
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
                val medication = documentSnapshot.toObject(NewUserMedication::class.java)
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

    override suspend fun addMedication(
        userId: String,
        medicationData: NewUserMedication,
    ): Result<String> {
        val dataToSave = medicationData.copy(userId = userId, id = "")

        return try {
            val documentRef =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(MEDICATIONS_SUBCOLLECTION)
                    .add(dataToSave)
                    .await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMedication(
        userId: String,
        medication: NewUserMedication,
    ): Result<Unit> {
        if (medication.id.isBlank()) {
            return Result.failure(
                IllegalArgumentException("Medication ID cannot be blank for update.")
            )
        }
        val dataToSave = medication.copy(userId = userId)

        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(MEDICATIONS_SUBCOLLECTION)
                .document(medication.id)
                .set(dataToSave)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMedication(userId: String, medicationId: String): Result<Unit> {
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

    override fun getTodaysIntakes(userId: String): Flow<List<NewUserMedication>> = callbackFlow {
        val todayEnd = LocalDate.now().plusDays(1).atStartOfDay().toTimestamp()
        val todayWeekDay = formatTimestampToWeekday(Timestamp.now()).uppercase()
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
                        val medications = snapshot.toObjects(NewUserMedication::class.java)
                        trySend(medications).isSuccess
                    }
                }
        awaitClose { listenerRegistration.remove() }
    }
}
