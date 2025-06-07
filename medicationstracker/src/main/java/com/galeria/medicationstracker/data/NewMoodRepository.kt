package com.galeria.medicationstracker.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface NewMoodRepository {

    fun observeUserMoods(userId: String): Flow<List<NewUserMood>>

    suspend fun getMood(userId: String, moodId: String): Result<NewUserMood>

    suspend fun addMood(userId: String, moodData: NewUserMood): Result<String>

    suspend fun updateMood(userId: String, mood: NewUserMood): Result<Unit>

    suspend fun deleteMood(userId: String, moodId: String): Result<Unit>
}

@Singleton
class NewMoodRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    NewMoodRepository {

    companion object {

        private const val USERS_COLLECTION = "User"
        private const val MOODS_SUBCOLLECTION = "moods"
    }

    override fun observeUserMoods(userId: String): Flow<List<NewUserMood>> = callbackFlow {
        val listenerRegistration =
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(MOODS_SUBCOLLECTION)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val moods = snapshot.toObjects(NewUserMood::class.java)
                        trySend(moods).isSuccess
                    }
                }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getMood(userId: String, moodId: String): Result<NewUserMood> {
        return try {
            val documentSnapshot =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(MOODS_SUBCOLLECTION)
                    .document(moodId)
                    .get()
                    .await()
            if (documentSnapshot.exists()) {
                val mood = documentSnapshot.toObject(NewUserMood::class.java)
                if (mood != null) {
                    Result.success(mood)
                } else {
                    Result.failure(Exception("Failed to parse mood data for ID: $moodId"))
                }
            } else {
                Result.failure(Exception("Mood not found with ID: $moodId for user: $userId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addMood(userId: String, moodData: NewUserMood): Result<String> {
        val dataToSave = moodData.copy(userId = userId, id = "")

        return try {
            val documentRef =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(MOODS_SUBCOLLECTION)
                    .add(dataToSave)
                    .await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateMood(userId: String, mood: NewUserMood): Result<Unit> {
        if (mood.id.isBlank()) {
            return Result.failure(IllegalArgumentException("Mood ID cannot be blank for update."))
        }
        val dataToSave = mood.copy(userId = userId)

        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(MOODS_SUBCOLLECTION)
                .document(mood.id)
                .set(dataToSave)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteMood(userId: String, moodId: String): Result<Unit> {
        if (moodId.isBlank()) {
            return Result.failure(IllegalArgumentException("Mood ID cannot be blank for delete."))
        }
        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(MOODS_SUBCOLLECTION)
                .document(moodId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
