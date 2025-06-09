package com.galeria.medicationstracker.data

import android.util.Log
import com.galeria.medicationstracker.utils.toTimestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

interface NewIntakeRepository {
    
    fun observeUserIntakes(userId: String): Flow<List<NewUserIntake>>

    suspend fun getUserIntake(userId: String, intakeId: String): Result<NewUserIntake>

    suspend fun addUserIntake(userId: String, intakeData: NewUserIntake): Result<String>

    suspend fun updateUserIntake(userId: String, intake: NewUserIntake): Result<Unit>

    suspend fun deleteUserIntake(userId: String, intakeId: String): Result<Unit>
    
    suspend fun fetchIntakeStatus(
        intake: NewUserMedication,
        uid: String
    ): Result<Int>
}

@Singleton
class NewIntakeRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    NewIntakeRepository {

    companion object {

        private const val USERS_COLLECTION = "User"
        private const val INTAKES_SUBCOLLECTION = "intakes"
    }

    override fun observeUserIntakes(userId: String): Flow<List<NewUserIntake>> = callbackFlow {
        val listenerRegistration =
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(INTAKES_SUBCOLLECTION)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val intakes = snapshot.toObjects(NewUserIntake::class.java)
                        trySend(intakes).isSuccess
                    }
                }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getUserIntake(userId: String, intakeId: String): Result<NewUserIntake> {
        return try {
            val documentSnapshot =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INTAKES_SUBCOLLECTION)
                    .document(intakeId)
                    .get()
                    .await()
            if (documentSnapshot.exists()) {
                val intake = documentSnapshot.toObject(NewUserIntake::class.java)
                if (intake != null) {
                    Result.success(intake)
                } else {
                    Result.failure(Exception("Failed to parse intake data for ID: $intakeId"))
                }
            } else {
                Result.failure(Exception("Intake not found with ID: $intakeId for user: $userId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addUserIntake(userId: String, intakeData: NewUserIntake): Result<String> {
        val dataToSave = intakeData.copy(userId = userId, id = "")

        return try {
            val documentRef =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(INTAKES_SUBCOLLECTION)
                    .add(dataToSave)
                    .await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUserIntake(userId: String, intake: NewUserIntake): Result<Unit> {
        if (intake.id.isBlank()) {
            return Result.failure(IllegalArgumentException("Intake ID cannot be blank for update."))
        }
        val dataToSave = intake.copy(userId = userId)

        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(INTAKES_SUBCOLLECTION)
                .document(intake.id)
                .set(dataToSave)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteUserIntake(userId: String, intakeId: String): Result<Unit> {
        if (intakeId.isBlank()) {
            return Result.failure(IllegalArgumentException("Intake ID cannot be blank for delete."))
        }
        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(INTAKES_SUBCOLLECTION)
                .document(intakeId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun fetchIntakeStatus(
        medication: NewUserMedication,
        uid: String,
    ): Result<Int> {
        val todayStart = LocalDate.now().atStartOfDay().toTimestamp()
        val todayEnd = LocalDate.now().plusDays(1).atStartOfDay().toTimestamp()
        
        return try {
            val querySnapshot =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(uid)
                    .collection(INTAKES_SUBCOLLECTION)
                    .whereEqualTo("medicationId", medication.id)
                    .whereGreaterThanOrEqualTo("timestamp", todayStart)
                    .whereLessThan("timestamp", todayEnd)
                    .limit(1)
                    .get()
                    .await()
            
            if (!querySnapshot.isEmpty) {
                if (
                    querySnapshot.toObjects(NewUserIntake::class.java)[0].status.toString() ==
                    "TAKEN"
                )
                    Result.success(2)
                else Result.success(1)
            } else {
                Result.success(0)
            }
        } catch (e: Exception) {
            Log.e("checkIntake", "Error fetching intake data", e)
            Result.success(-1)
        }
        TODO("Not yet implemented")
    }
}
