package com.galeria.medicationstracker.ui.screens.medications.mediinfo

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medicationstracker.data.repository.NewMedicationRepository
import com.galeria.medicationstracker.data.source.network.AuthRepository
import com.galeria.medicationstracker.data.source.network.NetworkIntake
import com.galeria.medicationstracker.data.source.network.NetworkMedication
import com.galeria.medicationstracker.navigation.MedicationScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationDetailsUiState(
    val medication: NetworkMedication? = null,
    val medicationIntakes: List<NetworkIntake> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class ViewMedicationViewModel
@Inject
constructor(
    private val medicationRepository: NewMedicationRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MedicationDetailsUiState())
    val uiState = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch {
            try {
                val uid = authRepository.getUserId().getOrThrow()
                val args =
                    savedStateHandle.toRoute<MedicationScreen.ViewMedication>()
                val medId = args.medicationId
                
                getMedDetails(medId, uid.toString())
            } catch (e: Exception) {
                Log.e("checkIntake", "Error fetching intake data", e)
            }
        }
    }
    
    private fun getMedDetails(medId: String? = null, uid: String? = null) {
        viewModelScope.launch {
            try {
                val result = medicationRepository.getMedication(
                    uid.toString(),
                    medId.toString()
                ).getOrThrow()
                
                _uiState.update { it.copy(medication = result) }
            } catch (e: Exception) {
                Log.e("checkIntake", "Error fetching intake data", e)
            }
        }
    }
}
