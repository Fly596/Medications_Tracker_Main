package com.galeria.medtracker2.feature.meds.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.notification.ScheduleNotification
import com.galeria.medtracker2.feature.meds.data.local.schedule.MedicationRegimenDao
import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimentDomain
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import com.galeria.medtracker2.feature.meds.domain.ScheduledDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

data class AddMedicationScreenState(
    val name: String = "",
    val dose: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val selectedTime: String = "",
    val startDateLong: Long = 0,
    val endDateLong: Long = 0,
    val selectedTimeInt: Pair<Int, Int> = Pair(0, 0),
)

data class AddMedUiState(
    val name: String = "",
    val dose: String = "",
    val startDate: Long? = null,
    val endDate: Long? = null,
    val intakeTime: Pair<Int, Int> = Pair(0, 0),
    val startDateString: String = "",
    val endDateString: String = "",
    val intakeTimeString: String = "",
    val isStartDatePickerVisible: Boolean = false,
    val isEndDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
)

@HiltViewModel
class AddMedicationVM
@Inject
constructor(
    private val repository: MedsRepository,
    private val medRegRepository: MedicationRegimenRepo,
    private val scheduleNotification: ScheduleNotification,
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    fun updateName(input: String) {
        viewModelScope.launch { _state.update { it.copy(name = input) } }
    }

    fun updateDose(input: String) {
        viewModelScope.launch { _state.update { it.copy(dose = input) } }
    }

    fun updateStartDate(input: Long?) {
        viewModelScope.launch {
            val parsedDate = DateTimeUtils.fromTimestampToLocalDateTime(input)
            val formattedDate = parsedDate.format(DateTimeUtils.dateFormatter)

            _state.update { it.copy(startDateString = formattedDate, startDate = input) }
        }
    }

    fun updateEndDate(input: Long?) {
        viewModelScope.launch {
            val parsedDate = DateTimeUtils.fromTimestampToLocalDateTime(input)
            val formattedDate = parsedDate.format(DateTimeUtils.dateFormatter)

            _state.update { it.copy(endDateString = formattedDate, endDate = input) }
        }
    }

    fun updateTime(time: Pair<Int, Int>) {

        viewModelScope.launch {
            val formattedTime = "%02d:%02d".format(time.first, time.second)

            _state.update { it.copy(intakeTime = time, intakeTimeString = formattedTime) }
        }
    }

    fun addMedication(context: Context) {
        // TODO
        viewModelScope.launch {
            // Установка уведомления приема.
            /*                scheduleNotification.scheduleNotification(
                context = context,
                timePickerState =
                    TimePickerState(
                        state.value.intakeTime.first,
                        state.value.intakeTime.second,
                        false,
                    ),
                datePickerState =
                    DatePickerState(
                        locale = CalendarLocale.getDefault(),
                        initialSelectedDateMillis = state.value.startDateLong,
                    ),
                title = "",
            )*/
            // Добавление лекарства в БД.
            val newMedication =
                    MedicationDomain(
                        id = UUID.randomUUID(),
                        name = _state.value.name,
                        creationDate = Instant.now(),
                    )
            repository.addMedication(newMedication)
            createSchedule()
        }
    }

    fun createSchedule() {
        viewModelScope.launch {
            val start = _state.value.startDate
            val end = _state.value.endDate
            val startConv = DateTimeUtils.fromTimestampToLocalDate(start)
            val endConv = DateTimeUtils.fromTimestampToLocalDate(end)
            val daysBetween = ChronoUnit.DAYS.between(endConv, startConv).toInt()

            val medId = UUID.randomUUID()
            val medRegId = UUID.randomUUID()
            // Добавление графика приема лекарства.
            medRegRepository.addRegiment(
                regiment =
                        MedicationRegimentDomain(
                            id = medRegId,
                            medicationId = medId,
                            doseMg = _state.value.dose.toDouble(),
                            startDate = Instant.ofEpochMilli(_state.value.startDate ?: 0),
                            endDate = Instant.ofEpochMilli(_state.value.endDate ?: 0),
                        )
            )

            val selHour = _state.value.intakeTime.first
            val selMin = _state.value.intakeTime.second
            var curDate = startConv
            for (i in 0..daysBetween) {
                val schId = UUID.randomUUID()
                val dateTimeInst = DateTimeUtils.fromDateTimeValues(curDate, selHour, selMin)
                medRegRepository.addSchedule(
                    schedule = ScheduledDomain(
                        id = schId,
                        medicationRegimentId = medRegId,
                        scheduledIntakeDateTime = dateTimeInst
                    )
                )
                curDate.plusDays(1)
            }
        }
    }
}

interface MedicationRegimenRepo {

    suspend fun addRegiment(regiment: MedicationRegimentDomain)
    suspend fun addSchedule(schedule: ScheduledDomain)
}

class MedicationRegimenRepoImp
@Inject
constructor(private val medicationRegimenDao: MedicationRegimenDao) : MedicationRegimenRepo {

    override suspend fun addRegiment(regiment: MedicationRegimentDomain) {}
    override suspend fun addSchedule(schedule: ScheduledDomain) {}
}
