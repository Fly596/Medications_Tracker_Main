package com.galeria.medicationstracker.feature.medications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.old.MedicationUnit
import com.galeria.medicationstracker.domain.medications.model.Medication
import com.galeria.medicationstracker.domain.medications.repository.MedicationRepository
import com.galeria.medicationstracker.utils.DateTimeUtils
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeParseException
import javax.inject.Inject

/**
 * Immutable UI State representing the state of the Add Medication screen.
 */
data class AddMedicationUiState(
    val medName: String = "",
    val medForm: String = MedicationForm.TABLET.name,
    val medStrength: String = "",
    val medUnit: MedicationUnit = MedicationUnit.MG,
    val medStartDate: Timestamp? = Timestamp.now(),
    val medEndDate: Timestamp? = Timestamp.now(),
    val medIntakeTime: String = "08:00",
    val medNotes: String = "",
    val showDatePicker: Boolean = false,
    val showTimePicker: Boolean = false,
    val intakeDays: List<String> = emptyList(),
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class AddMedicationViewModel @Inject constructor(
    private val repository: MedicationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddMedicationUiState())
    val uiState: StateFlow<AddMedicationUiState> = _uiState.asStateFlow()

    /**
     * Validates user inputs and persists the new medication entity to the repository.
     */
    fun addMedication() {
        val currentState = _uiState.value

        // Input validation
        if (currentState.medName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Medication name cannot be empty.") }
            return
        }

        val strengthVal = currentState.medStrength.toFloatOrNull()
        if (strengthVal == null || strengthVal <= 0f) {
            _uiState.update { it.copy(errorMessage = "Please enter a valid dosage strength.") }
            return
        }

        if (currentState.intakeDays.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Please select at least one day for intake.") }
            return
        }

        val parsedForm = try {
            MedicationForm.valueOf(currentState.medForm.uppercase())
        } catch (_: Exception) {
            MedicationForm.TABLET
        }

        val parsedTime = try {
            LocalTime.parse(currentState.medIntakeTime, DateTimeUtils.timeFormatter)
        } catch (_: DateTimeParseException) {
            LocalTime.of(8, 0)
        }

        val medication = Medication(
            id = "",
            name = currentState.medName.trim(),
            dosage = "${currentState.medStrength} ${currentState.medUnit.name.lowercase()}",
            form = parsedForm,
            startDate = DateTimeUtils.timestampToLocalDate(currentState.medStartDate),
            endDate = DateTimeUtils.timestampToLocalDate(currentState.medEndDate),
            daysOfWeek = currentState.intakeDays,
            intakeTime = parsedTime,
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            val result = repository.addMedication(medication)
            result.onSuccess {
                _uiState.update { it.copy(isSaving = false, isSaved = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessage = error.localizedMessage ?: "Failed to save medication."
                    )
                }
            }
        }
    }

    fun updateStartDate(input: Timestamp?) {
        _uiState.update { it.copy(medStartDate = input ?: Timestamp.now()) }
    }

    fun updateEndDate(input: Timestamp?) {
        _uiState.update { it.copy(medEndDate = input ?: Timestamp.now()) }
    }

    fun updateMedName(newName: String) {
        _uiState.update { it.copy(medName = newName, errorMessage = null) }
    }

    fun updateMedForm(newForm: String) {
        _uiState.update { it.copy(medForm = newForm) }
    }

    fun updateMedStrength(newStrength: String) {
        _uiState.update { it.copy(medStrength = newStrength, errorMessage = null) }
    }

    fun updateMedStrength(newStrength: Float) {
        _uiState.update { it.copy(medStrength = newStrength.toString(), errorMessage = null) }
    }

    fun updateMedUnit(newUnit: MedicationUnit) {
        _uiState.update { it.copy(medUnit = newUnit) }
    }

    fun updateIntakeTime(newTime: String) {
        _uiState.update { it.copy(medIntakeTime = newTime) }
    }

    fun updateMedNotes(newNotes: String) {
        _uiState.update { it.copy(medNotes = newNotes) }
    }

    fun updateSelectedDays(input: List<String>) {
        _uiState.update { it.copy(intakeDays = input, errorMessage = null) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}