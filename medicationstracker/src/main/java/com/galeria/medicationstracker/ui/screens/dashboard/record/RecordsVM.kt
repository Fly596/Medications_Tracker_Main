package com.galeria.medicationstracker.ui.screens.dashboard.record

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.UserIntake
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RecordsUiState(val intakes: List<UserIntake> = emptyList())

class RecordsVM : ViewModel() {

    private var _uiState = MutableStateFlow(RecordsUiState())
    val uiState = _uiState.asStateFlow()
    val db = FirestoreService.db

    init {
        fetchUserIntakes()
    }

    private fun fetchUserIntakes() {
        viewModelScope.launch {
            db.collection("User")
                .document("${FirebaseAuth.getInstance().currentUser?.email}")
                .collection("intakes")
                .orderBy("dateTime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { result ->
                    val intakes = result.toObjects(UserIntake::class.java)

                    _uiState.value = _uiState.value.copy(intakes = intakes)
                }
                .addOnFailureListener { exp -> println("Error fetching intakes: ${exp.message}") }
        }
    }
}
