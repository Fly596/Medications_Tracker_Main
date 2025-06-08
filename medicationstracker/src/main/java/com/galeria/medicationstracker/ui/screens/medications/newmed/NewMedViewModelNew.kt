package com.galeria.medicationstracker.ui.screens.medications.newmed

import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.NewMedicationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val intakeDays: List<String> = emptyList(),
)


class NewMedViewModelNew @Inject constructor(
    private val repository: NewMedicationRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(NewMedUiStateNew())
    val uiState: StateFlow<NewMedUiStateNew> = _uiState.asStateFlow()
    
    fun updateStartDate(input: Long) {
        val date = Date(input)
        _uiState.value = _uiState.value.copy(medStartDate = date)
    }
    
    fun updateEndDate(input: Long) {
        val date = Date(input)
        
        _uiState.value = _uiState.value.copy(medEndDate = date)
    }
    
    fun updateMedName(newName: String) {
        _uiState.value = _uiState.value.copy(medName = newName)
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