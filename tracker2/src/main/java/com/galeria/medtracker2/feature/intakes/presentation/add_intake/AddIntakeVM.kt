package com.galeria.medtracker2.feature.intakes.presentation.add_intake

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.Dose
import com.galeria.medtracker2.domain.model.IntakeDomain
import com.galeria.medtracker2.domain.repository.IntakesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID
import javax.inject.Inject

data class AddIntakeUiState(
    val dosage: String = "",
    // TODO: ДОПИСАТЬ ВЫБОР ЕД. СЧИСЛЕНИЯ.
    val unit: String = "MILLIGRAM",
    val selectedTime: LocalTime = LocalTime.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val isDatePickerOpen: Boolean = false,
    val isTimePickerOpen: Boolean = false,
    val medicationId: UUID = UUID.randomUUID(),
    val isLoading: Boolean = false,
    val isSavedSuccess: Boolean = false,
    val errorMessage: String? = null,
)

sealed interface AddIntakeUiEvent {
    data class OnDosageChanged(val dosage: String) : AddIntakeUiEvent
    data class OnUnitChanged(val unit: String) : AddIntakeUiEvent
    data class OnDateSelected(val date: LocalDate) : AddIntakeUiEvent
    data class OnTimeSelected(val time: LocalTime) : AddIntakeUiEvent
    data object OpenDatePicker : AddIntakeUiEvent
    data object DismissDatePicker : AddIntakeUiEvent
    data object OpenTimePicker : AddIntakeUiEvent
    data object DismissTimePicker : AddIntakeUiEvent
    data object ConfirmAndSave : AddIntakeUiEvent
}

@HiltViewModel
class AddIntakeVM @Inject
constructor(
    private val intakeRepository: IntakesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddIntakeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        val medicationId = savedStateHandle.get<String>("medicationId")
        if (medicationId != null) {
            _uiState.update { it.copy(medicationId = UUID.fromString(medicationId)) }
        }
    }

    fun onEvent(event: AddIntakeUiEvent) {
        when (event) {
            is AddIntakeUiEvent.OnDosageChanged -> {
                _uiState.update { it.copy(dosage = event.dosage) }
            }

            is AddIntakeUiEvent.OnUnitChanged -> {
                _uiState.update { it.copy(unit = event.unit) }
            }

            is AddIntakeUiEvent.OnDateSelected -> {
                _uiState.update { it.copy(selectedDate = event.date, isDatePickerOpen = false) }
            }

            is AddIntakeUiEvent.OnTimeSelected -> {
                _uiState.update { it.copy(selectedTime = event.time, isTimePickerOpen = false) }
            }

            AddIntakeUiEvent.OpenDatePicker -> {
                _uiState.update { it.copy(isDatePickerOpen = true) }
            }

            AddIntakeUiEvent.DismissDatePicker -> {
                _uiState.update { it.copy(isDatePickerOpen = false) }
            }

            AddIntakeUiEvent.OpenTimePicker -> {
                _uiState.update { it.copy(isTimePickerOpen = true) }
            }

            AddIntakeUiEvent.DismissTimePicker -> {
                _uiState.update { it.copy(isTimePickerOpen = false) }
            }

            AddIntakeUiEvent.ConfirmAndSave -> saveSchedule()
        }
    }

    private fun saveSchedule() {
        val state = _uiState.value
        val instant = DateTimeUtils.combineDateAndTime(state.selectedDate, state.selectedTime)

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            intakeRepository.insertIntake(
                IntakeDomain(
                    id = 0,
                    medicationId = state.medicationId,
                    dose = Dose(
                        state.dosage.toDouble(), WeightUnits.valueOf(
                            state.unit
                        )
                    ),
                    cost = null,
                    intakeDateTime = instant
                )
            )
            _uiState.update { it.copy(isLoading = false, isSavedSuccess = true) }
        }
    }
}