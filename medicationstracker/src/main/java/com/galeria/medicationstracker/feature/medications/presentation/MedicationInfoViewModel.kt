package com.galeria.medicationstracker.feature.medications.presentation

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medicationstracker.core.domain.repository.MedicationRepository
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.data.NewUserIntake
import com.galeria.medicationstracker.domain.medications.model.Medication
import com.galeria.medicationstracker.navigation.NavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MedicationInfoUiState(
    //val medication: NewUserMedication? = null,
    val medication: Medication? = null,
    val medicationIntakes: List<NewUserIntake> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class MedicationInfoViewModel
@Inject
constructor(
    private val newMedicationRepository: NewMedicationRepository,
    private val medicationRepository: MedicationRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MedicationInfoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val uid = authRepository.getUserId().getOrNull()
                val medId = savedStateHandle.get<String>("medicationId")
                    ?: runCatching { savedStateHandle.toRoute<NavRoute.ViewMedication>().medicationId }.getOrNull()
                if (uid != null && medId != null) {
                    getMedDetails(medId, uid)
                }
            } catch (e: Exception) {
                Log.e("checkIntake", "Error fetching intake data", e)
            }
        }
    }

    fun loadMedicationDetails(medId: String) {
        viewModelScope.launch {
            try {
                val uid = authRepository.getUserId().getOrNull()
                if (uid != null) {
                    getMedDetails(medId, uid)
                }
            } catch (e: Exception) {
                Log.e("checkIntake", "Error fetching intake data", e)
            }
        }
    }

    private fun getMedDetails(medId: String? = null, uid: String? = null) {
        viewModelScope.launch {
            try {
                val result = medicationRepository.getMedication(
                    medId.toString()
                ).getOrThrow()

                _uiState.update { it.copy(medication = result) }
            } catch (e: Exception) {
                Log.e("checkIntake", "Error fetching intake data", e)
            }
        }
    }
    /*   private fun getMedDetails(medId: String? = null, uid: String? = null) {
           viewModelScope.launch {
               try {
                   val result = newMedicationRepository.getMedication(
                       uid.toString(),
                       medId.toString()
                   ).getOrThrow()

                   _uiState.update { it.copy(medication = result) }
               } catch (e: Exception) {
                   Log.e("checkIntake", "Error fetching intake data", e)
               }
           }
       }*/
}
