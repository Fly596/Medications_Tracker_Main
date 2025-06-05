package com.galeria.medicationstracker.data.imp

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface NewNoteRepository {

    fun observeUserNotes(userId: String): Flow<List<NewUserNote>>

    suspend fun getNote(userId: String, noteId: String): Result<NewUserNote>

    suspend fun addNote(userId: String, noteData: NewUserNote): Result<String>

    suspend fun updateNote(userId: String, note: NewUserNote): Result<Unit>

    suspend fun deleteNote(userId: String, noteId: String): Result<Unit>
}

@Singleton
class NewNoteRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore) :
    NewNoteRepository {

    companion object {

        private const val USERS_COLLECTION = "User"
        private const val NOTES_SUBCOLLECTION = "notes"
    }

    override fun observeUserNotes(userId: String): Flow<List<NewUserNote>> = callbackFlow {
        val listenerRegistration =
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTES_SUBCOLLECTION)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val notes = snapshot.toObjects(NewUserNote::class.java)
                        trySend(notes).isSuccess
                    }
                }
        awaitClose { listenerRegistration.remove() }
    }

    override suspend fun getNote(userId: String, noteId: String): Result<NewUserNote> {
        return try {
            val documentSnapshot =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(NOTES_SUBCOLLECTION)
                    .document(noteId)
                    .get()
                    .await()
            if (documentSnapshot.exists()) {
                val note = documentSnapshot.toObject(NewUserNote::class.java)
                if (note != null) {
                    Result.success(note)
                } else {
                    Result.failure(Exception("Failed to parse note data for ID: $noteId"))
                }
            } else {
                Result.failure(Exception("Note not found with ID: $noteId for user: $userId"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun addNote(userId: String, noteData: NewUserNote): Result<String> {
        val dataToSave = noteData.copy(userId = userId, id = "")

        return try {
            val documentRef =
                firestore
                    .collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(NOTES_SUBCOLLECTION)
                    .add(dataToSave)
                    .await()
            Result.success(documentRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNote(userId: String, note: NewUserNote): Result<Unit> {
        if (note.id.isBlank()) {
            return Result.failure(IllegalArgumentException("Note ID cannot be blank for update."))
        }
        val dataToSave = note.copy(userId = userId)

        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTES_SUBCOLLECTION)
                .document(note.id)
                .set(dataToSave)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNote(userId: String, noteId: String): Result<Unit> {
        if (noteId.isBlank()) {
            return Result.failure(IllegalArgumentException("Note ID cannot be blank for delete."))
        }
        return try {
            firestore
                .collection(USERS_COLLECTION)
                .document(userId)
                .collection(NOTES_SUBCOLLECTION)
                .document(noteId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
