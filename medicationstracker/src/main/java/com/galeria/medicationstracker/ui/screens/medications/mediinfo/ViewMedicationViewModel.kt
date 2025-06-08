package com.galeria.medicationstracker.ui.screens.medications.mediinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewUserMedication
import com.galeria.medicationstracker.utils.navigation.RoutesOld.PatientRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationDetailsUiState(
    val medication: NewUserMedication? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class ViewMedicationViewModel @Inject constructor(
    private val medicationRepository: NewMedicationRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // private var _messageState = MutableStateFlow(savedStateHandle.toRoute<PatientRoutes.PatientViewMedication>().medicationId)
    // val messageState: StateFlow<String> = _messageState
    private val medDetails: PatientRoutes.PatientViewMedication =
        savedStateHandle.toRoute()
    private val _uiState = MutableStateFlow(MedicationDetailsUiState())
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
                
                getMedDetails()
            }
        }
    }
    
    private fun getMedDetails() {
        viewModelScope.launch {
            val temp = medicationRepository.getMedication(
                currentUserId,
                medDetails.medicationId
            )
            
            if (temp.isSuccess) {
                _uiState.update {
                    it.copy(medication = temp.getOrNull())
                }
            }
        }
    }
}
