package com.galeria.medtracker2.feature.medication.presentation.edit_med

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.model.Money
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.navigation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import kotlin.math.roundToLong

data class EditMedUiState(
    val name: String = "",
    val selectedUnit: WeightUnits = WeightUnits.MILLIGRAM,
    val price: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class EditMedVM @Inject constructor(
    val medicationRepository: MedicationRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val medicationId: UUID? = try {
        val (medicationId) = savedStateHandle.toRoute<AppRoutes.EditMedication>()
        UUID.fromString(medicationId)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Log.e(TAG, "Failed to parse medicationId from SavedStateHandle", e)
        null
    }

    init {
        viewModelScope.launch {
            if (medicationId == null) {
                _state.update { it.copy(errorMessage = "Medication ID not found") }
                return@launch
            }
            val medication = medicationRepository.getMedication(medicationId)
            if (medication == null) {
                _state.update { it.copy(errorMessage = "Medication not found") }
                return@launch
            }
            _state.update {
                it.copy(
                    name = medication.name,
                    selectedUnit = medication.unit,
                    price = medication.defaultPricePerUnit?.cents.toString() ?: ""
                )
            }
        }
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
            if (medicationId == null) {
                _state.update { it.copy(errorMessage = "Medication not found") }
                return@launch
            }
            val parsedPrice = currentState.price.replace(',', '.').toDoubleOrNull() ?: 0.0
            val med = MedicationDomain(
                id = medicationId,
                name = currentState.name,
                unit = WeightUnits.valueOf(currentState.selectedUnit.name),
                defaultPricePerUnit = Money(
                    cents = (parsedPrice).roundToLong(),
                ),
                creationTimestamp = Instant.now()
            )

            medicationRepository.updateMedication(med)

        }
    }
}