package com.galeria.medtracker2.feature.tracker.presentation.add_med

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AddMedUiState(
    val name: String = "Adderall",
    val dose: String = "50",
    val unit: String = "mg",
    val price: String = "10",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class AddMedVM
@Inject
constructor() : ViewModel() {

    val units = listOf("mg", "g")
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

    fun updateUnit(input: String) {
        if (input in units) {
            _state.update { it.copy(unit = input) }
        }
    }

    fun updatePrice(input: String) {
        // digits only.
        if (input.all { char -> char.isDigit() }) {
            _state.update { it.copy(price = input) }
        }
    }
}
