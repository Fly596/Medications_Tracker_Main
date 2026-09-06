package com.galeria.medtracker2.feature.medication.presentation.view_med

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medtracker2.domain.model.IntakeDomain
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.repository.IntakesRepository
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.navigation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * UI State for the Medication Details screen.
 */
sealed interface ViewMedUiState {

    data object Loading : ViewMedUiState

    data object Empty : ViewMedUiState

    data class Success(
        val medication: MedicationDomain,
        val intakes: List<IntakeDomain> = emptyList(),
        val totalDosage: Double = 0.0,
        val totalPrice: Long = 0L
    ) : ViewMedUiState

    data class Error(val message: String) : ViewMedUiState
}

/**
 * ViewModel responsible for managing and providing state for the [ViewMedScreen].
 * Observes medication details and associated intake history reactively from repositories.
 */
@HiltViewModel
class ViewMedVM @Inject constructor(
    private val medicationRepository: MedicationRepository,
    intakeRepository: IntakesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val medicationId: UUID? = try {
        val (medicationId) = savedStateHandle.toRoute<AppRoutes.MedicationDetails>()
        UUID.fromString(medicationId)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        Log.e(TAG, "Failed to parse medicationId from SavedStateHandle", e)
        null
    }

    val uiState: StateFlow<ViewMedUiState> = if (medicationId == null) {
        flowOf(ViewMedUiState.Error("Invalid or missing medication ID"))
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ViewMedUiState.Loading,
            )
    } else {
        combine(
            medicationRepository.observeMedications().map { medications ->
                medications.find { it.id == medicationId }
            },
            intakeRepository.getAllIntakes().map { intakes ->
                intakes.filter { it.medicationId == medicationId }
            },
            intakeRepository.getTotalDosage(medicationId).map { totalDosage ->
                totalDosage
            }
        ) { medication, intakes, totalDosage ->
            if (medication == null) {
                ViewMedUiState.Empty
            } else {
                ViewMedUiState.Success(
                    medication = medication,
                    intakes = intakes,
                    totalDosage = totalDosage
                )
            }
        }
            .catch { e ->
                if (e is CancellationException) throw e
                Log.e(TAG, "Error observing medication details", e)
                emit(
                    ViewMedUiState.Error(
                        e.localizedMessage ?: "Failed to load medication details"
                    )
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = ViewMedUiState.Loading,
            )
    }

    /**
     * Deletes the specified medication from the repository.
     */
    fun deleteMedication(id: UUID) {
        viewModelScope.launch {
            try {
                medicationRepository.removeMedication(id)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                Log.e(TAG, "Error deleting medication with id: $id", e)
            }
        }
    }

    companion object {

        private const val TAG = "ViewMedVM"
    }
}
