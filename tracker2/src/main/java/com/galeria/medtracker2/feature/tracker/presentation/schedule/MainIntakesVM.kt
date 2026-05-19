package com.galeria.medtracker2.feature.tracker.presentation.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.IntakeLogDomain
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import com.galeria.medtracker2.domain.repository.IntakesRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class ScheduleUiState(
    val plannedIntakes: List<ScheduledIntakeDetails> = emptyList(),
    val todaysIntakes: List<ScheduledIntakeDetails> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MainIntakesVM
@Inject
constructor(
    private val regimentsRepository: MedicationsCourseRepository,
    private val intakesRepository: IntakesRepository,
) : ViewModel() {

    val uiState: StateFlow<ScheduleUiState> =
        regimentsRepository
            .getFullSchedule()
            .distinctUntilChanged()
            .map { allRecords ->
                val now = LocalDate.now()
                ScheduleUiState(
                    plannedIntakes = allRecords,
                    todaysIntakes =
                        allRecords.filter {
                            DateTimeUtils.fromLongToLocalDate(it.scheduledTimestamp) == now
                        },
                    isLoading = false,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = ScheduleUiState(isLoading = true),
            )

    private val _state = MutableStateFlow(ScheduleUiState())
    val state = _state.asStateFlow()

    // region old
    //    init {
    //        loadSchedule()
    //    }
    //    private fun loadSchedule() {
    //        viewModelScope.launch {
    //            val temp = mutableListOf<ScheduledIntakeDetails>()
    //
    //            // TODO: Добавить отфильтрованные запросы на сегодня от репозитория, а не в UI.
    //            regimentsRepository.getFullSchedule().distinctUntilChanged().collect { schedule ->
    //                schedule.forEach {
    //                    if (
    //                        DateTimeUtils.fromTimestampToDate(it.scheduledTimestamp) ==
    // LocalDate.now()
    //                    ) {
    //                        temp.add(it)
    //                    }
    //                }
    //
    //                _state.update {
    //                    it.copy(plannedIntakes = schedule, isLoading = false, todaysIntakes =
    // temp)
    //                }
    //            }
    //        }
    //    }
    // endregion

    // TODO: Добавить обработку ошибок.
    fun checkIntake(isTaken: Boolean, intake: ScheduledIntakeDetails, intakeDateTime: Instant) {
        viewModelScope.launch {
            try {
                val log =
                    IntakeLogDomain(
                        id = UUID.randomUUID(),
                        plannedIntakeId = intake.plannedIntakeId,
                        actualTimestamp = intakeDateTime,
                        isTaken = isTaken,
                        notes = "",
                    )
                intakesRepository.addIntake(log)
            } catch (e: Exception) {
                Log.e("MainIntakesVM", "Failed to mark intake", e)
            }
        }

        //        val newIntake =
        //            IntakeLogDomain(
        //                id = UUID.randomUUID(),
        //                plannedIntakeId = intake.plannedIntakeId,
        //                actualTimestamp = intakeDateTime,
        //                isTaken = isTaken,
        //                notes = "",
        //            )
        //        viewModelScope.launch { intakesRepository.addIntake(newIntake) }
    }

    // TODO: сделать получения расписания на сегодня отдельным запросом к БД, а не фильтрацией в UI.
    private fun getScheduleForToday(schedule: List<ScheduledIntakeDetails>) {
        val temp = mutableListOf<ScheduledIntakeDetails>()
        viewModelScope.launch {
            schedule.forEach {
                if (DateTimeUtils.fromLongToLocalDate(it.scheduledTimestamp) == LocalDate.now()) {
                    temp.add(it)
                }
            }
        }
    }
}
