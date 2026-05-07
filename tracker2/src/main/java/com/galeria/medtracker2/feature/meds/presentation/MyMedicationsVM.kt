package com.galeria.medtracker2.feature.meds.presentation

import androidx.lifecycle.ViewModel
import com.galeria.medtracker2.core.common.data.Medication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class MyMedicationsUiState(
    val isLoading: Boolean = true,
    val medications: List<Medication> = emptyList(),
)

@HiltViewModel
class MyMedicationsVM @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MyMedicationsUiState())
    val state = _state.asStateFlow()

    init {
        loadMedications()
    }

    private fun loadMedications() {
        _state.value = MyMedicationsUiState(isLoading = false, medications = emptyList())
    }
}
