package com.galeria.medtracker2.feature.medication.presentation.meds_list

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

sealed interface MedsUiState {
    data object Loading : MedsUiState

    data object Empty : MedsUiState

    data class Success(val medsList: List<MedicationDomain>) : MedsUiState

    data class Error(val message: String) : MedsUiState
}

data class MyMedsUiState(
    val medications: List<MedicationDomain> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MyMedsVM
@Inject
constructor(
    private val medicationRepository: MedicationRepository,
) : ViewModel() {

    // Получение лекарств в реальном времени.
    val uiState: StateFlow<MyMedsUiState> =
            medicationRepository
                .observeMedications()
                .distinctUntilChanged()
                .map { allMedications ->
                    MyMedsUiState(medications = allMedications, isLoading = false)
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = MyMedsUiState(isLoading = true),
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
