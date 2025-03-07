package com.galeria.medicationstracker.data

import android.util.Log
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService.db
import com.galeria.medicationstracker.utils.toLocalDateTime
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

interface UserRepository {

    suspend fun addUser()
    suspend fun deleteUser()
    suspend fun addIntake(intake: UserIntake)
    suspend fun getUserData(): UserProfile
    suspend fun updateUserData(user: UserProfile)
    suspend fun getUserDrugs(): List<UserMedication>
    suspend fun getUserIntakes(uid: String): List<UserIntake>
    suspend fun saveNote(note: Note)
    suspend fun getNotes(): List<Note>
    fun getUserIntakesFlow(uid: String): Flow<List<UserIntake>>

}

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : UserRepository {

    override suspend fun saveNote(note: Note) {
        firestore.collection("User")
            .document("${auth.currentUser?.email}")
            .collection("notes")
            .document("${note.title}_${note.date?.toLocalDateTime()?.dayOfYear}")
            .set(
                note
            )
    }

    override suspend fun getNotes(): List<Note> {
        return try {
            val userRef = firestore.collection("User")
                .document(auth.currentUser?.email.toString())
                .collection("notes")
                .get()
                .await()
            userRef.toObjects(Note::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun addIntake(intake: UserIntake) {
        firestore.collection("User")
            .document("${auth.currentUser?.email}")
            .collection("intakes")
            .document("${intake.medicationName}_${intake.dateTime?.toLocalDateTime()?.dayOfYear}")
            .set(
                intake
            )
    }

    override suspend fun addUser() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser() {
        TODO("Not yet implemented")
    }

    override suspend fun getUserData(): UserProfile {
        return try {
            val userRef = firestore.collection("User")
                .document(auth.currentUser?.email.toString())
                .get()
                .await()
            userRef.toObject(UserProfile::class.java)!!
        } catch (e: Exception) {
            UserProfile()
        }
    }

    override suspend fun updateUserData(user: UserProfile) {
        firestore.collection("User").document(auth.currentUser?.email.toString())
            .set(user)
            .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("TAG", "Error writing document", e) }

    }

    override suspend fun getUserIntakes(uid: String): List<UserIntake> {
        return try {
            val userRef = firestore.collection("User")
                .document(auth.currentUser?.email.toString())
                .collection("intakes")
                .get()
                .await()
            userRef.toObjects(UserIntake::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override fun getUserIntakesFlow(uid: String): Flow<List<UserIntake>> {
        return callbackFlow {
            val listenerRegistration = firestore.collection("User")
                .document(auth.currentUser?.email.toString())
                .collection("intakes")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .whereEqualTo("uid", uid)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        // Handle error
                        return@addSnapshotListener
                    }

                    if (value != null) {
                        val userIntakes = value.toObjects(UserIntake::class.java)
                        trySend(userIntakes)
                    }
                }
            // Clean up the listener when the flow is cancelled
            awaitClose {
                listenerRegistration.remove()
            }
        }
    }

    override suspend fun getUserDrugs(): List<UserMedication> {
        return try {
            val querySnapshot = db.collection("UserMedication")
                .whereEqualTo("uid", auth.currentUser?.uid)
                .get()
                .await()
            querySnapshot.toObjects(UserMedication::class.java)
        } catch (e: Exception) {
            emptyList()
        }

    }
}