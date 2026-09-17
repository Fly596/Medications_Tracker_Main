package com.galeria.medtracker2.feature.medication.presentation.edit_med

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.domain.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EditMedUiState(
    val name: String = "",
    val selectedUnit: WeightUnits = WeightUnits.MILLIGRAM,
    val dose: String = "",
    val price: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class EditMedVM @Inject constructor(
    val medicationRepository: MedicationRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    init {
        // TODO:
    }

    private val _state = MutableStateFlow(EditMedUiState())
    val state = _state.asStateFlow()

    fun updateName(input: String) {
        _state.update { it.copy(name = input) }
    }

    fun onUnitSelected(units: WeightUnits) {
        _state.update { it.copy(selectedUnit = units) }
    }

    fun updatePrice(input: String) {
        // digits only.
        _state.update { it.copy(price = input) }
    }

    fun updateMedication() {
        if (_state.value.isLoading) return
        val currentState = _state.value

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

        }
    }
}