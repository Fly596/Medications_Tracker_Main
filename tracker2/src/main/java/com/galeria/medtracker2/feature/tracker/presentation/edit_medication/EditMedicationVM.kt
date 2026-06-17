package com.galeria.medtracker2.feature.tracker.presentation.edit_medication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import com.galeria.medtracker2.feature.tracker.presentation.medication.MedicationUiState
import com.galeria.medtracker2.navigation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

data class EditMedUiState(
    val name: String = "Adderall",
    val dose: String = "50",
    val startDateMillis: Long = DateTimeUtils.fromLocalDateToLong(LocalDate.now()),
    val endDateMillis: Long = DateTimeUtils.fromLocalDateToLong(LocalDate.now()),
    val intakeTimes: List<LocalTime> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class EditMedicationVM
@Inject
constructor(
    private val regimentsRepository: MedicationsCourseRepository,
    private val medicationRepository: MedicationRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(EditMedUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val args = savedStateHandle.toRoute<AppRoutes.MedicationDetailsRoute>()
                val medId = UUID.fromString(args.medicationId)
                getMedication(medId)
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.localizedMessage) }
            }
        }
    }

    fun getMedication(id: UUID) {
        viewModelScope.launch {
            try {
                val med = regimentsRepository.getCourseById(id)
            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMessage = e.localizedMessage)
                }
            }
            _state.update { it.copy(isLoading = false) }
        }
    }

    fun deleteMedication(id: UUID) {
        viewModelScope.launch {
            try {
                medicationRepository.removeMedication(id)
            } catch (e: Exception) {
                _state.value = MedicationUiState.Error("${e.localizedMessage}")
            }
        }
    }
}
