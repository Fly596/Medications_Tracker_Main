package com.galeria.medicationstracker.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.NewUser
import com.galeria.medicationstracker.data.NewUserIntake
import com.galeria.medicationstracker.data.NewUserMedication
import com.galeria.medicationstracker.data.NewUserRepository
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions.merge
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

// ? TODO: decide what to show on screens.
data class ProfileScreenUiState(
    val user: NewUser? = null,
    val age: Int = 0,
    val weight: Float = 0.0f,
    val height: Float = 0.0f,
    val name: String = "",
    val intakes: List<NewUserIntake> = emptyList(),
    val medications: List<NewUserMedication> = emptyList(),
)

@HiltViewModel
class ProfileVM @Inject constructor(
    private val repository: NewUserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileScreenUiState())
    val uiState = _uiState.asStateFlow()
    val db = FirestoreService.db
    private val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    private val currentUserId = firebaseAuth.currentUser?.uid

    init {
        /*         viewModelScope.launch {
                    val repoUser = repository.getUserData()
                    val repoIntakes = repository.getUserIntakes(currentUserId.toString())
                    _uiState.value = _uiState.value.copy(user = repoUser, intakes = repoIntakes)
                }
        
                viewModelScope.launch {
                    // val user = repository.getUserData()
                    // val medications = repository.getUserDrugs()
                     *//*             _uiState.value =
                            _uiState.value.copy(user = user, medications = medications) *//*

            repository.getUserIntakesFlow((currentUserId.toString()))
                .collect { intakes ->
                    _uiState.value = _uiState.value.copy(
                        intakes = intakes,
                    )
                }
        } */
    }

    /*     private fun fetchUserData() {
            viewModelScope.launch {
                val userRef = db.collection("User")
                    .document(currentUser?.email.toString())
                val source = Source.DEFAULT
                
                try {
                    userRef.get(source)
                        .addOnSuccessListener { result ->
                            val user = result.toObject(User::class.java)
                            _uiState.value = _uiState.value.copy(user = user)
                        }
                        .addOnFailureListener { exp ->
                            println("Error fetching user data: ${exp.message}")
                        }
                } catch (e: Exception) {
                    println("Error fetching user data: ${e.message}")
                }
            }
            
        } */

    fun updateAgeFirestore() {
        val userRef = db.collection("User")
            .document(currentUser?.email.toString())

        userRef.set(
            mapOf("age" to _uiState.value.age),
            merge()
        )
            .addOnSuccessListener {
                Log.d("ProfileVM", "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("ProfileVM", "Error adding document", e)
            }
    }

    fun updateWeightFirestore() {
        val userRef = db.collection("User")
            .document(currentUser?.email.toString())

        userRef.set(
            mapOf("weight" to _uiState.value.weight),
            merge()
        )
            .addOnSuccessListener {
                Log.d("ProfileVM", "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("ProfileVM", "Error adding document", e)
            }
    }

    fun updateHeightFirestore() {
        val userRef = db.collection("User")
            .document(currentUser?.email.toString())

        userRef.set(
            mapOf("height" to _uiState.value.height),
            merge()
        )
            .addOnSuccessListener {
                Log.d("ProfileVM", "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("ProfileVM", "Error adding document", e)
            }
    }

    fun updateNameFirestore() {
        val userRef = db.collection("User")
            .document(currentUser?.email.toString())
        userRef.set(
            mapOf("name" to _uiState.value.name),
            merge()
        )
            .addOnSuccessListener {
                Log.d("ProfileVM", "DocumentSnapshot added successfully!")
            }
            .addOnFailureListener { e ->
                Log.w("ProfileVM", "Error adding document", e)
            }
    }

    fun updateName(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun updateAge(age: Int) {
        _uiState.value = _uiState.value.copy(age = age)
    }

    fun updateWeight(weight: Float) {
        _uiState.value = _uiState.value.copy(weight = weight)
    }

    fun updateHeight(height: Float) {
        _uiState.value = _uiState.value.copy(height = height)
    }
}
