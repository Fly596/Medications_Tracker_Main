package com.galeria.medtracker2.feature.tracker.presentation.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface MedicationsUiState {
    data object Loading : MedicationsUiState

    data object Empty : MedicationsUiState

    data class Success(val medsList: List<MedicationCourseSummary>) : MedicationsUiState

    data class Error(val message: String) : MedicationsUiState
}

data class MyMedicationsUiState(
    val medsList: List<MedicationCourseSummary> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MyMedicationsVM
@Inject
constructor(
    private val regimentsRepository: MedicationsCourseRepository,
    private val medicationRepository: MedicationRepository,
) : ViewModel() {

    // Получение лекарств в реальном времени.
    val uiState: StateFlow<MyMedicationsUiState> =
        regimentsRepository
            .getActiveCourses()
            .distinctUntilChanged()
            .map { allMedications ->
                MyMedicationsUiState(medsList = allMedications, isLoading = false)
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MyMedicationsUiState(isLoading = true),
            )
}
