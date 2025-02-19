package com.galeria.medicationstracker.data

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
    suspend fun getUserData(uid: String): User
    suspend fun updateUserData(user: User)
    suspend fun getUserDrugs(uid: String): List<UserMedication>
    suspend fun getUserIntakes(uid: String): List<UserIntake>
    fun getUserIntakesFlow(uid: String): Flow<List<UserIntake>>
    
}

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
) : UserRepository {
    
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
    
    override suspend fun getUserData(uid: String): User {
        return try {
            val userRef = firestore.collection("User")
                .document(auth.currentUser?.email.toString())
                .get()
                .await()
            userRef.toObject(User::class.java)!!
        } catch (e: Exception) {
            User()
        }
        
    }
    
    override suspend fun updateUserData(user: User) {
        TODO("Not yet implemented")
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
    
    override suspend fun getUserDrugs(uid: String): List<UserMedication> {
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
}