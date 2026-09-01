package com.galeria.medtracker2.feature.tracker.presentation.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface MedicationsUiState {
    data object Loading : MedicationsUiState

    data object Empty : MedicationsUiState

    data class Success(val medsList: List<MedicationDomain>) : MedicationsUiState

    data class Error(val message: String) : MedicationsUiState
}

data class MyMedicationsUiState(
    val medications: List<MedicationDomain> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MyMedicationsVM
@Inject
constructor(
    private val medicationRepository: MedicationRepository,
) : ViewModel() {

    // Получение лекарств в реальном времени.
    val uiState: StateFlow<MyMedicationsUiState> =
            medicationRepository
                .getAllMedications()
                .distinctUntilChanged()
                .map { allMedications ->
                    MyMedicationsUiState(medications = allMedications, isLoading = false)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = MyMedicationsUiState(isLoading = true),
                )
    /*    val uiState: StateFlow<MyMedicationsUiState> =
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
                    )*/
}
