package com.galeria.medicationstracker.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.Medication
import com.galeria.medicationstracker.data.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddMedicationsScreenUiState(
    val medicationName: String = "",
    val classType: String = "",
    val form: String = "",
    val strength: Float = 0f,
    val unit: String = "",
    val requiresPrescription: Boolean = false,
    val manufacturer: String = "",
    val sideEffects: List<String> = emptyList(),
    val contraindications: List<String> = emptyList(),
    val dosageInstructions: String = "",
)

@HiltViewModel
class AddMedicationsViewModel @Inject constructor(private val repository: MedicationRepository) :
    ViewModel() {
    
    private val _uiState = MutableStateFlow(AddMedicationsScreenUiState())
    val uiState = _uiState.asStateFlow()
    
    fun addMedication() {
        viewModelScope.launch {
            repository.addMedication(
                Medication(
                    name = _uiState.value.medicationName,
                    classType = _uiState.value.classType,
                    form = _uiState.value.form,
                    strength = _uiState.value.strength,
                    unit = _uiState.value.unit,
                    requiresPrescription = _uiState.value.requiresPrescription,
                    manufacturer = _uiState.value.manufacturer,
                    sideEffects = _uiState.value.sideEffects,
                    contraindications = _uiState.value.contraindications,
                    dosageInstructions = _uiState.value.dosageInstructions,
                )
            )
        }
    }
    
    fun updateMedicationName(name: String) {
        _uiState.value = _uiState.value.copy(medicationName = name)
    }
    
    fun updateClassType(type: String) {
        _uiState.value = _uiState.value.copy(classType = type)
    }
    
    fun updateForm(form: String) {
        _uiState.value = _uiState.value.copy(form = form)
    }
    
    fun updateStrength(strength: Float) {
        _uiState.value = _uiState.value.copy(strength = strength)
    }
    
    fun updateUnit(unit: String) {
        _uiState.value = _uiState.value.copy(unit = unit)
    }
    
    fun updateRequiresPrescription(requiresPrescription: Boolean) {
        _uiState.value = _uiState.value.copy(requiresPrescription = requiresPrescription)
    }
    
    fun updateManufacturer(manufacturer: String) {
        _uiState.value = _uiState.value.copy(manufacturer = manufacturer)
    }
    
    fun updateSideEffects(input: String) {
        _uiState.value = _uiState.value.copy(sideEffects = _uiState.value.sideEffects + input)
    }
    
    fun updateContraindications(contraindications: String) {
        _uiState.value =
            _uiState.value.copy(
                contraindications = _uiState.value.contraindications + contraindications
            )
    }
    
    fun updateDosageInstructions(dosageInstructions: String) {
        _uiState.value = _uiState.value.copy(dosageInstructions = dosageInstructions)
    }
}
