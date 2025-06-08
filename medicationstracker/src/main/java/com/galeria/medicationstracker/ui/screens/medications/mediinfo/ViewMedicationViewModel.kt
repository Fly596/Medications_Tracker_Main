package com.galeria.medicationstracker.ui.screens.medications.mediinfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewUserMedication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    
    private val _uiState = MutableStateFlow(MedicationDetailsUiState())
    val uiState = _uiState.asStateFlow()
    private val medicationId: String? = savedStateHandle[]
}
