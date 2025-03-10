package com.galeria.medicationstracker.ui.screens.dashboard.moodtracker

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.UserRepository
import com.galeria.medicationstracker.utils.FirestoreFunctions.FirestoreService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MoodTrackerUiState(
    val mood: Int = 0,
    val notes: String? = null
)

@HiltViewModel
class MoodTrackerVM @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    val db = FirestoreService.db
    private val _uiState = MutableStateFlow(MoodTrackerUiState())
    val uiState: StateFlow<MoodTrackerUiState> = _uiState.asStateFlow()
    private val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser

    fun addMood(mood: Int) {
        viewModelScope.launch {
            val user = FirebaseAuth.getInstance().currentUser
            val userId = user?.uid

            val moodEntry = hashMapOf(
                "mood" to mood,
                "timestamp" to Timestamp.now(),
                "notes" to uiState.value.notes
            )

            if (userId != null) {
                db.collection("User").document(currentUser?.email.toString())
                    .collection("moodTracker")
                    .add(moodEntry)
                    .addOnSuccessListener { documentReference ->
                        Log.d(
                            "MoodTrackerVM",
                            "DocumentSnapshot added with ID: ${documentReference.id}"
                        )
                    }
                    .addOnFailureListener { e ->
                        Log.w("MoodTrackerVM", "Error adding document", e)
                    }
            }
        }
    }

    fun updateMood(mood: Int) {
        _uiState.value = _uiState.value.copy(mood = mood)
    }

    fun updateNotes(notes: String) {
        _uiState.value = _uiState.value.copy(notes = notes)
    }


}