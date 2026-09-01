package com.galeria.medtracker2.feature.tracker.presentation.add_med

import androidx.lifecycle.ViewModel
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.domain.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AddMedUiState(
    val name: String = "Adderall",
    val selectedUnit: WeightUnits = WeightUnits.MILLIGRAM,
    val dose: String = "50",
    val price: String = "10",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isDropDownExpanded: Boolean = false
)

@HiltViewModel
class AddMedVM
@Inject
constructor(
    val medicationRepository: MedicationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    fun updateName(input: String) {
        _state.update { it.copy(name = input) }
    }

    fun updateDose(input: String) {
        // digits only.
        if (input.all { char -> char.isDigit() }) {
            _state.update { it.copy(dose = input) }
        }
    }

    fun onUnitSelected(units: WeightUnits) {
        _state.update { it.copy(selectedUnit = units, isDropDownExpanded = false) }
    }

    fun toggleDropDown() {
        _state.update { it.copy(isDropDownExpanded = !it.isDropDownExpanded) }
    }

    fun updatePrice(input: String) {
        // digits only.
        if (input.all { char -> char.isDigit() }) {
            _state.update { it.copy(price = input) }
        }
    }
}
