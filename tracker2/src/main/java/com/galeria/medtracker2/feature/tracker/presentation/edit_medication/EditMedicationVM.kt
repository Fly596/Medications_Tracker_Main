package com.galeria.medtracker2.feature.tracker.presentation.edit_medication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import com.galeria.medtracker2.feature.tracker.domain.GetIntakeTimesUseCase
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
    private val medicationsCourseRepository: MedicationsCourseRepository,
    private val medicationRepository: MedicationRepository,
    private val getIntakeTimesUseCase: GetIntakeTimesUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(EditMedUiState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
              val args = savedStateHandle.toRoute<AppRoutes.EditMedication>()
                val medId = UUID.fromString(args.medicationId)
                getMedication(medId)
            } catch (e: Exception) {
                _state.update { it.copy(errorMessage = e.localizedMessage) }
            }
        }
    }

    // TODO: протестировать.
    fun getMedication(medId: UUID) {
        viewModelScope.launch {
            try {
                val courseSummary = medicationsCourseRepository.getCourseSummaryByMedId(medId)
                if (courseSummary == null) {
                    _state.update { it.copy(errorMessage = "Medication not found") }
                } else {
                    // Получаем список intakeTimes.
                    val intakeTimes = getIntakeTimesUseCase.invoke(medId)

                    _state.update {
                        it.copy(
                            name = courseSummary.name,
                            dose = courseSummary.doseMg.toString(),
                            startDateMillis = courseSummary.startDate,
                            endDateMillis = courseSummary.endDate,
                            intakeTimes = intakeTimes,
                        )
                    }
                }
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
                _state.update { it.copy(errorMessage = e.localizedMessage) }
            }
        }
    }
}
