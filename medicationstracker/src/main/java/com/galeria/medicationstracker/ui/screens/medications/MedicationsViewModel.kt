package com.galeria.medicationstracker.ui.screens.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewUserMedication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationsUiState(
    val userMedications: List<NewUserMedication> = emptyList()
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
    private lateinit var currentUserId: String
    private lateinit var currentUserEmail: String

    init {
        viewModelScope.launch {
            // Получение id и почты пользователя.
            val emailResult = authRepository.getUserEmail()
            val uidResult = authRepository.getUserId()

            if (emailResult.isSuccess && uidResult.isSuccess) {
                currentUserEmail = emailResult.getOrNull().toString()
                currentUserId = uidResult.getOrNull().toString()
            }
        }
        fetchMedications(currentUserId)
    }

    private fun fetchMedications(uid: String) {
        viewModelScope.launch {
            medicationRepository.observeUserMedications(uid).collect { medications ->
                _uiState.update { it.copy(userMedications = medications) }
            }
        }
    }

    // Удаление лекарства из Firestore.
    // TODO:
    fun deleteMedicationFromFirestore(medId: String) {
        viewModelScope.launch { medicationRepository.deleteMedication(currentUserId, medId) }
    }
}
