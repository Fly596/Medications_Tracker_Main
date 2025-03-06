package com.galeria.medicationstracker.ui.doctor.patients

import androidx.lifecycle.ViewModel

/*
data class PatientsListUiState(
    val patients: List<User> = emptyList(),
    val appointments: List<Appointment> = emptyList(),
    val selectedPatient: User? = null,
    val currentUser: User? = null
)
*/

class PatientsListViewModel : ViewModel() {

/*     private val db = FirestoreService.db
    private val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val userID = firebaseAuth.currentUser?.uid
    val userLogin = firebaseAuth.currentUser?.email
    private val _uiState = MutableStateFlow(PatientsListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Fetch appointments when the ViewModel is created.
        fetchDoctorAppointments()
        fetchUser()
    }

    private fun fetchUser() {
        viewModelScope.launch {
            try {
                if (userID == null) {
                    return@launch
                }
                db.collection("User")
                    .whereEqualTo("uid", userID)
                    .get()
                    .addOnSuccessListener {
                        val user = it.toObjects(User::class.java)
                        _uiState.value = _uiState.value.copy(currentUser = user[0])
                    }
                    .addOnFailureListener {
                        println("Error finding documents: $it")
                    }

            } catch (e: FirebaseFirestoreException) {
                // Log or handle the error appropriately.
                println("Error finding documents: $e")
                Log.e("ERROR", e.toString())
            }
        }
    }

    private fun fetchDoctorAppointments() {
        viewModelScope.launch {
            try {
                // Query for appointments where the doctor field matches the current user ID.
                val docRef = db.collection("Appointments")
                    .whereEqualTo("doctor", userID)
                    .get()
                    .await()
                    .toObjects(Appointment::class.java)
                Log.e("SUCCESS", docRef.toString())
                // Update the UI state with the fetched appointments.
                updateAppointments(docRef)

            } catch (e: FirebaseFirestoreException) {
                // Log or handle the error appropriately.
                println("Error finding documents: $e")
                Log.e("ERROR", e.toString())
            }
        }
    }

    private fun fetchPatients() {
        viewModelScope.launch {
            val docRef = db.collection("User")

            docRef
                .whereEqualTo("type", "PATIENT")
                .get()
                .addOnSuccessListener { result ->
                    val patients = result.toObjects(User::class.java)

                    _uiState.value = _uiState.value.copy(patients = patients)
                }
                .addOnFailureListener { exception ->
                    println("Error finding documents: $exception")
                }
        }

    }

    fun updatePatients(value: List<User>) {
        _uiState.value = _uiState.value.copy(patients = value)
    }

    fun updateAppointments(value: List<Appointment>) {
        _uiState.value = _uiState.value.copy(appointments = value)
    } */
}