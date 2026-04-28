package com.galeria.medtracker2.feature.intakes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.feature.intakes.domain.IntakeDomain
import com.galeria.medtracker2.feature.intakes.domain.IntakesRepository
import com.galeria.medtracker2.feature.meds.data.local.schedule.FullSchedule
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID
import javax.inject.Inject

data class ScheduleUiState(
    val plannedIntakes: List<FullSchedule> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MainIntakesVM
@Inject
constructor(
    private val regimentsRepository: MedicationRegimenRepo,
    private val intakesRepository: IntakesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleUiState())
    val state = _state.asStateFlow()

    init {
        loadSchedule()
    }

    fun checkIntake(status: Boolean, intake: FullSchedule, intakeDateTime: Instant) {
        val newIntake = IntakeDomain(
            id = UUID.randomUUID(),
            medicationScheduleId = intake.idDateTime,
            actualIntakeDateTime = intakeDateTime,
            status = status,
            notes = ""
        )
        viewModelScope.launch {
            intakesRepository.addIntake(newIntake)

        }
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            regimentsRepository.getFullSchedule().distinctUntilChanged().collect { schedule ->
                _state.update { it.copy(plannedIntakes = schedule, isLoading = false) }
            }
        }
    }
}
