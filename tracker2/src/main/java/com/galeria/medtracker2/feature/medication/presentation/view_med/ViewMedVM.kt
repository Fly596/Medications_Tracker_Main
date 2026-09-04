package com.galeria.medtracker2.feature.medication.presentation.view_med

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.navigation.AppRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed interface ViewMedUiState {
    data object Loading : ViewMedUiState

    data object Empty : ViewMedUiState

    data class Success(val medication: MedicationDomain) : ViewMedUiState

    data class Error(val message: String) : ViewMedUiState
}

@HiltViewModel
class ViewMedVM
@Inject
constructor(
    private val medicationRepository: MedicationRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ViewMedUiState>(ViewMedUiState.Empty)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = ViewMedUiState.Loading
            try {
                val args = savedStateHandle.toRoute<AppRoutes.MedicationDetails>()
                val medId = UUID.fromString(args.medicationId)
                getMedication(medId)
            } catch (e: Exception) {
                _uiState.value = ViewMedUiState.Error("${e.localizedMessage}")
                Log.e("medication", "Error fetching medication data", e)
            }
        }
    }

    fun getMedication(id: UUID) {
        viewModelScope.launch {
            try {
                val medication = medicationRepository.getMedicationById(id)
                if (medication == null) {
                    _uiState.value = ViewMedUiState.Empty
                } else {
                    _uiState.value = ViewMedUiState.Success(medication)
                }
            } catch (e: Exception) {
                _uiState.value = ViewMedUiState.Error("${e.localizedMessage}")
            }
        }
    }

    fun deleteMedication(id: UUID) {
        viewModelScope.launch {
            try {
                medicationRepository.removeMedication(id)
            } catch (e: Exception) {
                _uiState.value = ViewMedUiState.Error("${e.localizedMessage}")
            }
        }
    }
}
/*
data class MedicationUiState(
    val medication: MedicationCourseSummary? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class MedicationVM(
    private val regimentsRepository: MedicationsCourseRepository,
    private val medicationRepository: MedicationRepository,
) : ViewModel() {

    // Получение лекарств в реальном времени.
    val uiState: StateFlow<MedicationUiState> =
        regimentsRepository
            .getActiveCourses()
            .distinctUntilChanged()
            .map { allMedications ->
                MedicationUiState(medication = allMedications, isLoading = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MedicationUiState(isLoading = true),
            )
}
*/
