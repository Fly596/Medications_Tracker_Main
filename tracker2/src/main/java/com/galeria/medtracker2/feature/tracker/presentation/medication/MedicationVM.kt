
package com.galeria.medtracker2.feature.tracker.presentation.medication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

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
