package com.galeria.medtracker2.feature.meds.presentation

import androidx.lifecycle.ViewModel
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddMedicationVM @Inject constructor(
    private val repository: MedsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedicationScreenState())
    val state = _state.asStateFlow()
}