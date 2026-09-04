package com.galeria.medtracker2.feature.medication.presentation.add_med

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.math.round

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
        if (input.all { char -> char.isDigit() || char == '.' || char == ',' }) {
            _state.update { it.copy(price = input) }
        }
    }

    fun addMedication() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val currentState = _state.value
                val name = currentState.name.trim()
                val p = currentState.price.trim().toDouble()
                val price = if (currentState.price.isEmpty()) 0.0 else (
                        round(currentState.price.toDouble() * 100))
                val med =
                        MedicationDomain(
                            UUID.randomUUID(),
                            name,
                            price.toInt(),
                            unit = currentState.selectedUnit.name,
                            creationTimestamp = Instant.now()
                        )
                medicationRepository.addMedication(med)
                _state.update { it.copy(isLoading = false) }
            } catch (e: CancellationException) {
                // ВАЖНО: Никогда не глуши CancellationException, иначе сломаешь отмену корутин!
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, errorMessage = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}
