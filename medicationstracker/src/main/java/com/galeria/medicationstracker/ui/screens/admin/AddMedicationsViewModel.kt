package com.galeria.medicationstracker.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medicationstracker.data.InteractionType
import com.galeria.medicationstracker.data.Medication
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.MedicationRepository
import com.galeria.medicationstracker.data.MedicationUnit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddMedicationsScreenUiState(
    val drugId: String = "",
    val medicationName: String = "",
    val classType: String = "", // Переименовал class, потому что это зарезервированное слово в Kotlin
    val form: MedicationForm = MedicationForm.UNKNOWN,
    val strength: Float = 0f,
    val unit: MedicationUnit = MedicationUnit.MG,
    val requiresPrescription: Boolean = false,
    val sideEffects: List<String> = emptyList(),
    val contraindications: List<String> = emptyList(),
    val interactions: Map<String, InteractionType> = emptyMap(),
    val dosageInstructions: String = "",
    val duration: Float = 0f,
    val medication1: String = "",
    val medication2: String = "",
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
    
    fun updateForm(form: MedicationForm) {
        _uiState.value = _uiState.value.copy(form = form)
    }
    
    fun updateStrength(strength: Float) {
        _uiState.value = _uiState.value.copy(strength = strength)
    }
    
    fun updateUnit(unit: MedicationUnit) {
        _uiState.value = _uiState.value.copy(unit = unit)
    }
    
    fun updateRequiresPrescription(requiresPrescription: Boolean) {
        _uiState.value = _uiState.value.copy(requiresPrescription = requiresPrescription)
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
