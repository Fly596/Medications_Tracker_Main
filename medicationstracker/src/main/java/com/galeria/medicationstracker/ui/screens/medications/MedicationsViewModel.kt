package com.galeria.medicationstracker.ui.screens.medications

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.network.NetworkMedication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationsUiState(
    val userMedications: List<NetworkMedication> = emptyList()
    // val medication: Medication = Medication()
)

@HiltViewModel
class MedicationsViewModel
@Inject
constructor(
    private val medicationRepository: NewMedicationRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MedicationsUiState())
    val uiState = _uiState.asStateFlow()
    
    // private lateinit var currentUserId: String
    // private lateinit var currentUserEmail: String
    init {
        viewModelScope.launch {
            try {
                val uid = authRepository.getUserId().getOrThrow()
                fetchMedications(uid.toString())
            } catch (e: Exception) {
                Log.e("checkIntake", "Error fetching intake data", e)
            }
        }
    }
    
    private fun fetchMedications(uid: String) {
        viewModelScope.launch {
            medicationRepository.observeUserMedications(uid)
                .collect { medications ->
                    _uiState.update { it.copy(userMedications = medications) }
                }
        }
    }
    
    // Удаление лекарства из Firestore.
    // TODO:
    fun deleteMedicationFromFirestore(medId: String) {
        viewModelScope.launch {
            try {
                val uid = authRepository.getUserId().getOrThrow()
                medicationRepository.deleteMedication(uid.toString(), medId)
            } catch (e: Exception) {
                Log.e("ERROR REMOVE", "Error deleting medication", e)
            }
        }
    }
}
