package com.galeria.medtracker2.feature.meds.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.data.MedicationCourseSummary
import com.galeria.medtracker2.feature.meds.domain.MedicationScheduleIntakesRepository
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MyMedicationsUiState(
    val medsList: List<MedicationCourseSummary> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MyMedicationsVM
@Inject
constructor(
    private val regimentsRepository: MedicationScheduleIntakesRepository,
    private val medsRepository: MedsRepository,
) : ViewModel() {

    // Получение лекарств в реальном времени.
    val uiState: StateFlow<MyMedicationsUiState> =
        regimentsRepository
            .getRegimentsWithNameDoseDates()
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
