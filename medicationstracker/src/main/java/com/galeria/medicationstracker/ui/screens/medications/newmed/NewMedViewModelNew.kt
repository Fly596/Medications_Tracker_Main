package com.galeria.medicationstracker.ui.screens.medications.newmed

import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.core.domain.repository._MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Date
import javax.inject.Inject

data class NewMedUiStateNew(
  val medName: String = "",
  val medDosage: String = "",
  val medUnit: String = "",
  val form: String = "",
  val medStartDate: Date = Date(),
  val medEndDate: Date = Date(),
  val medIntakeTime: String = "",
  val showDatePicker: Boolean = false,
  val showTimePicker: Boolean = false,
  val intakeDays: List<String> = emptyList(),
)

@HiltViewModel
class NewMedViewModelNew @Inject constructor(
  private val repository: _MedicationRepository
) : ViewModel() {

  private val _uiState = MutableStateFlow(NewMedUiStateNew())
  val uiState = _uiState.asStateFlow()

  fun updateStartDate(input: Long) {
    val date = Date(input)
    _uiState.value = _uiState.value.copy(medStartDate = date)
  }

  fun updateEndDate(input: Long) {
    val date = Date(input)

    _uiState.value = _uiState.value.copy(medEndDate = date)
  }

  fun updateMedName(newName: String) {
    _uiState.update {
      it.copy(medName = newName)
    }
  }

  fun updateMedDosage(newStrength: Float) {
    _uiState.value =
        _uiState.value.copy(medDosage = newStrength.toString() /* .toFloat() */)
  }

  fun updateIntakeTime(newTime: String) {
    _uiState.value = _uiState.value.copy(medIntakeTime = newTime)
  }
  /*     fun isShowDateChecked(input: Boolean) {
          _uiState.value = _uiState.value.copy(showDatePicker = !input)
      }

      fun isShowTimeChecked(input: Boolean) {
          _uiState.value = _uiState.value.copy(showTimePicker = !input)
      } */
  fun updateSelectedDays(input: List<String>) {
    _uiState.value =
        _uiState.value.copy(intakeDays = _uiState.value.intakeDays + input)
  }
}