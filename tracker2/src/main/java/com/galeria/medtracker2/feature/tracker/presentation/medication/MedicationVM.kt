package com.galeria.medtracker2.feature.tracker.presentation.medication

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface MedicationUiState {
    data object Loading : MedicationUiState

    data object Empty : MedicationUiState

    data class Success(val medication: MedicationCourseSummary) : MedicationUiState

    data class Error(val message: String) : MedicationUiState
}

@HiltViewModel
class MedicationVM
@Inject
constructor(
    private val regimentsRepository: MedicationsCourseRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val uiState: StateFlow<MedicationUiState> =
        regimentsRepository
            .getActiveCourses()
            .distinctUntilChanged()
            .map { allMedications ->
                // Избавляемся от null/пустоты прямо на уровне маппинга
                val activeCourse = allMedications.firstOrNull() // или твоя логика выбора курса
                if (activeCourse != null) {
                    MedicationUiState.Success(activeCourse)
                } else {
                    MedicationUiState.Empty
                }
            }
            .catch { e -> emit(MedicationUiState.Error(e.localizedMessage ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MedicationUiState.Loading,
            )
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
