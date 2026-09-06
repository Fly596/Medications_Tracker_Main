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

/**
 * Represents the UI state for the Add Intake screen.
 *
 * @property dosage The dosage amount entered as text by the user.
 * @property unit The selected measurement unit ([WeightUnits]), defaulting to [WeightUnits.MILLIGRAM].
 * @property selectedTime The time selected for intake, defaulting to current time.
 * @property selectedDate The date selected for intake, defaulting to current date.
 * @property isDatePickerOpen Controls the visibility of the Date Picker dialog.
 * @property isTimePickerOpen Controls the visibility of the Time Picker dialog.
 * @property medicationId The UUID of the medication, retrieved from navigation arguments.
 * @property isLoading Indicates whether a save operation is currently in progress.
 * @property isSavedSuccess Indicates whether the intake record was saved successfully.
 * @property errorMessage Holds user-facing error messages when input validation or save operation fails.
 */
data class AddIntakeUiState(
  val dosage: String = "",
  val unit: WeightUnits = WeightUnits.MILLIGRAM,
  val selectedTime: LocalTime = LocalTime.now(),
  val selectedDate: LocalDate = LocalDate.now(),
  val isDatePickerOpen: Boolean = false,
  val isTimePickerOpen: Boolean = false,
  val medicationId: UUID? = null,
  val isLoading: Boolean = false,
  val isSavedSuccess: Boolean = false,
  val errorMessage: String? = null,
)

sealed interface AddIntakeUiEvent {
  data class OnDosageChanged(val dosage: String) : AddIntakeUiEvent
  data class OnUnitChanged(val unit: WeightUnits) : AddIntakeUiEvent
  data class OnDateSelected(val date: LocalDate) : AddIntakeUiEvent
  data class OnTimeSelected(val time: LocalTime) : AddIntakeUiEvent
  data object OpenDatePicker : AddIntakeUiEvent
  data object DismissDatePicker : AddIntakeUiEvent
  data object OpenTimePicker : AddIntakeUiEvent
  data object DismissTimePicker : AddIntakeUiEvent
  data object ConfirmAndSave : AddIntakeUiEvent
  data object ClearError : AddIntakeUiEvent
}

@HiltViewModel
class AddIntakeVM @Inject constructor(
  private val intakeRepository: IntakesRepository,
  savedStateHandle: SavedStateHandle,
) : ViewModel() {

  private val _uiState = MutableStateFlow(AddIntakeUiState())
  val uiState = _uiState.asStateFlow()

  init {
    // Safely retrieve and parse medicationId from SavedStateHandle
    val medicationIdStr = savedStateHandle.get<String>("medicationId")
    if (!medicationIdStr.isNullOrBlank()) {
      val parsedUuid = runCatching { UUID.fromString(medicationIdStr) }.getOrNull()
      if (parsedUuid != null) {
        _uiState.update { it.copy(medicationId = parsedUuid) }
      } else {
        _uiState.update {
          it.copy(errorMessage = "Invalid medication ID format provided.")
        }
      }
    }
  }

  fun onEvent(event: AddIntakeUiEvent) {
    when (event) {
      is AddIntakeUiEvent.OnDosageChanged -> {
        _uiState.update {
          it.copy(
            dosage = event.dosage,
            errorMessage = null
          )
        }
      }

      is AddIntakeUiEvent.OnUnitChanged -> {
        _uiState.update {
          it.copy(
            unit = event.unit,
            errorMessage = null
          )
        }
      }

      is AddIntakeUiEvent.OnDateSelected -> {
        _uiState.update {
          it.copy(
            selectedDate = event.date,
            isDatePickerOpen = false
          )
        }
      }

      is AddIntakeUiEvent.OnTimeSelected -> {
        _uiState.update {
          it.copy(
            selectedTime = event.time,
            isTimePickerOpen = false
          )
        }
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

      AddIntakeUiEvent.ClearError -> {
        _uiState.update { it.copy(errorMessage = null) }
      }
    }
  }

  private fun saveSchedule() {
    val currentState = _uiState.value

    // Prevent concurrent double-clicks while save operation is already running
    if (currentState.isLoading) return

    // 1. Validate target Medication ID presence
    val medId = currentState.medicationId
    if (medId == null) {
      _uiState.update {
        it.copy(errorMessage = "Medication ID is missing. Cannot save intake.")
      }
      return
    }

    // 2. Validate Dosage Input (supports both comma and dot decimal separators)
    val dosageValue = currentState.dosage.trim().replace(',', '.').toDoubleOrNull()
    if (dosageValue == null || dosageValue <= 0.0) {
      _uiState.update {
        it.copy(errorMessage = "Please enter a valid positive numeric dosage.")
      }
      return
    }

    // 3. Combine selected LocalDate and LocalTime into an Instant
    val instant = DateTimeUtils.combineDateAndTime(
      currentState.selectedDate,
      currentState.selectedTime
    )

    viewModelScope.launch {
      _uiState.update { it.copy(isLoading = true, errorMessage = null) }
      try {
        intakeRepository.insertIntake(
          IntakeDomain(
            id = 0,
            medicationId = medId,
            dose = Dose(
              amount = dosageValue,
              unit = currentState.unit
            ),
            cost = null,
            intakeDateTime = instant
          )
        )
        _uiState.update { it.copy(isLoading = false, isSavedSuccess = true) }
      } catch (e: Exception) {
        _uiState.update {
          it.copy(
            isLoading = false,
            errorMessage = e.localizedMessage ?: "Failed to save intake. Please try again."
          )
        }
      }
    }
  }
}
