package com.galeria.medtracker2.feature.meds.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.notification.ScheduleNotification
import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimentDomain
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import com.galeria.medtracker2.feature.meds.domain.ScheduledDateTimeDomain
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
    val name: String = "test",
    val dose: String = "56",
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
        _state.update { it.copy(name = input) }
    }

    fun updateDose(input: String) {
        _state.update { it.copy(dose = input) }
    }

    fun updateStartDate(input: Long?) {
        input?.let {
            val parsedDate = DateTimeUtils.fromTimestampToLocalDate(it)
            val formattedDate = parsedDate.format(DateTimeUtils.dateFormatter)
            _state.update { s -> s.copy(startDateString = formattedDate, startDate = it) }
        }
    }

    fun updateEndDate(input: Long?) {
        input?.let {
            val parsedDate = DateTimeUtils.fromTimestampToLocalDate(it)
            val formattedDate = parsedDate.format(DateTimeUtils.dateFormatter)
            _state.update { s -> s.copy(endDateString = formattedDate, endDate = it) }
        }
    }

    fun updateTime(time: Pair<Int, Int>) {
        val formattedTime = "%02d:%02d".format(time.first, time.second)
        _state.update { it.copy(intakeTime = time, intakeTimeString = formattedTime) }
    }

    fun addMedication(context: Context) {
        val currentState = _state.value

        // Базовая валидация
        if (
            currentState.name.isBlank() ||
            currentState.startDate == null ||
            currentState.endDate == null
        )
            return

        // TODO
        viewModelScope.launch {
            try {
                val newMedId = UUID.randomUUID()
                val newMedication =
                        MedicationDomain(
                            id = newMedId,
                            name = _state.value.name,
                            creationDate = Instant.now(),
                        )

                // Сначала сохраняем основную запись
                repository.addMedication(newMedication)

                // Затем создаем расписание
                createSchedule(newMedId, currentState)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addNotif(context: Context) {

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
        }
    }

    private suspend fun createSchedule(medId: UUID, currentState: AddMedUiState) {
        val startConv = DateTimeUtils.fromTimestampToLocalDate(currentState.startDate ?: 0)
        val endConv = DateTimeUtils.fromTimestampToLocalDate(currentState.endDate ?: 0)

        val daysBetween = ChronoUnit.DAYS.between(startConv, endConv).toInt()

        val medRegId = UUID.randomUUID()
        val doseValue = currentState.dose.toDoubleOrNull() ?: 0.0

        // Добавление режима (Regiment)
        medRegRepository.addRegiment(
            MedicationRegimentDomain(
                id = medRegId,
                medicationId = medId,
                doseMg = doseValue,
                startDate = Instant.ofEpochMilli(currentState.startDate!!),
                endDate = Instant.ofEpochMilli(currentState.endDate!!),
            )
        )

        val selHour = currentState.intakeTime.first
        val selMin = currentState.intakeTime.second

        var curDate = startConv

        for (i in 0..daysBetween) {
            val schId = UUID.randomUUID()

            // Используем объединение даты и времени в Instant
            val dateTimeInst = DateTimeUtils.fromDateTimeValues(curDate, selHour, selMin)

            medRegRepository.addSchedule(
                schedule =
                        ScheduledDateTimeDomain(
                        id = schId,
                        medicationRegimentId = medRegId,
                            scheduledIntakeDateTime = dateTimeInst,
                    )
            )

            // Присваиваем результат обратно переменной.
            curDate = curDate.plusDays(1)
        }

        // TODO: После создания всех записей в БД можно запланировать уведомления
        // scheduleNotification.planNext(medId)
    }
}
