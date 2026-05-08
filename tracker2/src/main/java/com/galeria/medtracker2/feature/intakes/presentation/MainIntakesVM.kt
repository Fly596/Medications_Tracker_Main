package com.galeria.medtracker2.feature.intakes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.common.data.FullSchedule
import com.galeria.medtracker2.feature.intakes.domain.IntakeLogDomain
import com.galeria.medtracker2.feature.intakes.domain.IntakesRepository
import com.galeria.medtracker2.feature.meds.domain.MedicationScheduleIntakesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class ScheduleUiState(
    val plannedIntakes: List<FullSchedule> = emptyList(),
    val todaysIntakes: List<FullSchedule> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MainIntakesVM
@Inject
constructor(
    private val regimentsRepository: MedicationScheduleIntakesRepository,
    private val intakesRepository: IntakesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleUiState())
    val state = _state.asStateFlow()

    init {
        loadSchedule()
    }

    // TODO: Добавить обработку ошибок.
    fun checkIntake(status: Boolean, intake: FullSchedule, intakeDateTime: Instant) {

        val newIntake = IntakeLogDomain(
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
            val temp = mutableListOf<FullSchedule>()

            // TODO: Добавить отфильтрованные запросы на сегодня от репозитория, а не в UI.
            regimentsRepository.getFullSchedule().distinctUntilChanged().collect { schedule ->
                schedule.forEach {
                    if (DateTimeUtils.fromTimestampToDate(
                            it.scheduledIntakeDateTime
                        ) == LocalDate.now()
                    ) {
                        temp.add(it)
                    }
                }

                _state.update {
                    it.copy(
                        plannedIntakes = schedule, isLoading = false, todaysIntakes = temp
                    )
                }
            }
        }
    }

    // TODO: сделать получения расписания на сегодня отдельным запросом к БД, а не фильтрацией в UI.
    private fun getScheduleForToday(schedule: List<FullSchedule>) {
        val temp = mutableListOf<FullSchedule>()
        viewModelScope.launch {
            schedule.forEach {
                if (DateTimeUtils.fromTimestampToDate(
                        it.scheduledIntakeDateTime
                    ) == LocalDate.now()
                ) {
                    temp.add(it)
                }
            }
        }

    }
}
