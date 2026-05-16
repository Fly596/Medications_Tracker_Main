package com.galeria.medtracker2.feature.meds.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.notification.data.ScheduleNotificationRepoImpl
import com.galeria.medtracker2.feature.meds.domain.MedicationCourseDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationScheduleIntakesRepository
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

data class AddMedUiState(
    val name: String = "test",
    val dose: String = "56",
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val intakeTimesNew: List<LocalTime> = emptyList(),

    val intakeTime: Pair<Int, Int> = Pair(0, 0),
    val intakeTimes: List<Pair<Int, Int>> = emptyList(),
    val startDateString: String = "Choose start date",
    val endDateString: String = "Choose end date",
    val intakeTimeString: String = "Choose time",
    val isStartDatePickerVisible: Boolean = false,
    val isEndDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
)

const val TAG: String = "MyActivity"
const val DEFAULT_SCHEDULE_DAYS: Int = 7

@HiltViewModel
class AddMedicationVM
@Inject
constructor(
    private val repository: MedsRepository,
    private val medRegRepository: MedicationScheduleIntakesRepository,
    private val notificationService: ScheduleNotificationRepoImpl,
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    // ui update.
    fun updateName(input: String) = _state.update { it.copy(name = input) }

    fun updateDose(input: String) = _state.update { it.copy(dose = input) }

    fun updateStartDate(millis: Long) = _state.update { it.copy(startDateMillis = millis) }
    fun updateEndDate(millis: Long) = _state.update { it.copy(endDateMillis = millis) }

    fun addTime(time: LocalTime) {
        // check for duplicates.
        if (time !in _state.value.intakeTimesNew) {
            _state.update { it.copy(intakeTimesNew = it.intakeTimesNew + time) }
        }
    }

    fun removeTime(time: LocalTime) {
        _state.update { it.copy(intakeTimesNew = it.intakeTimesNew - time) }
    }

    // region old
    fun updateStartDateOld(input: Long?) {
        val date = DateTimeUtils.fromTimestampToDate(input ?: Instant.now().toEpochMilli())
        _state.update {
            it.copy(
                startDateString = date.format(DateTimeUtils.dateFormatter),
                startDateMillis = input ?: Instant.now().toEpochMilli(),
            )
        }
    }

    fun updateEndDateOld(input: Long?) {
        input?.let {
            val date = DateTimeUtils.fromTimestampToDate(it)
            _state.update {
                it.copy(
                    endDateString = date.format(DateTimeUtils.dateFormatter), endDateMillis = input
                )
            }
        }
    }

    fun updateTime(time: Pair<Int, Int>) {
        val formattedTime = "%02d:%02d".format(time.first, time.second)
        val changedTimesList = _state.value.intakeTimes.toMutableList()
        changedTimesList.add(time)

        _state.update { st ->
            st.copy(
                intakeTime = time,
                intakeTimeString = formattedTime,
                intakeTimes = changedTimesList.toList(),
            )
        }
    }

    fun updateTimeEntities() {}

    // endregion

    /** Основной метод сохранения. Сначала создаем запись лекарства, затем генерируем расписание. */
    fun addMedication() {
        val currentState = _state.value
        if (currentState.name.isBlank()) return

        viewModelScope.launch {
            try {
                val medicationId = UUID.randomUUID()

                // Сначала сохраняем основную запись
                repository.addMedication(
                    MedicationDomain(medicationId, currentState.name, Instant.now())
                )

                // 2. Генерируем дни приема и ставим алармы
                generateScheduleEntries(medicationId, currentState)
            } catch (e: Exception) {
                // В продакшене здесь должен быть вывод ошибки пользователю (через SideEffect)
                Log.e("AddMedicationVM", "Error saving medication", e)
            }
        }
    }

    private suspend fun generateScheduleEntries(medicationId: UUID, currentState: AddMedUiState) {
        // Проверка, чтобы не создавать записи за прошедшие дни.
        val start =
            if (currentState.startDateMillis < Instant.now().toEpochMilli()) {
                DateTimeUtils.fromTimestampToDate(Instant.now().toEpochMilli())
            } else {
                DateTimeUtils.fromTimestampToDate(currentState.startDateMillis)
            }

        val end =
            DateTimeUtils.fromTimestampToDate(
                currentState.endDateMillis ?: currentState.startDateMillis.plus(
                    DEFAULT_SCHEDULE_DAYS
                )
            )

        // TODO: сделать, чтобы при входе в прилоюение подгружались данные по приемам на N дней
        // вперед.
        val daysCount = ChronoUnit.DAYS.between(start, end).toInt()
        val medicationCourseId = UUID.randomUUID()

        // Сохраняем общ инфу о курсе (режим приема).
        medRegRepository.addCourse(
            MedicationCourseDomain(
                id = medicationCourseId,
                medicationId = medicationId,
                doseMg = currentState.dose.toDoubleOrNull() ?: 0.0,
                startDate = Instant.ofEpochMilli(currentState.startDateMillis),
                endDate =
                    Instant.ofEpochMilli(
                        currentState.endDateMillis ?: currentState.startDateMillis.plus(
                            DEFAULT_SCHEDULE_DAYS
                        )
                    ),
            )
        )

        val dailyIntakes = currentState.intakeTimes
        var currentPointerDate = start
        // На каждый день.
        for (i in 0..daysCount) {
            Log.e(TAG, "for loop, i = $i")

            // На каждое время приемов.
            dailyIntakes.forEachIndexed { index, value ->
                Log.e(TAG, "foreach loop, index = $index, value = $value")
                val plannedIntakeId = UUID.randomUUID()
                val intakeTimeMoment =
                    DateTimeUtils.fromDateTimeValues(
                        currentPointerDate,
                        value.first,
                        value.second,
                    )

                // 1. Сохраняем прием в БД.
                medRegRepository.addPlannedIntake(
                    PlannedIntakeDomain(
                        id = plannedIntakeId,
                        courseId = medicationCourseId,
                        scheduledTimestamp = intakeTimeMoment,
                    )
                )

                // 2. Планируем уведомление (только если время еще не прошло)
                if (intakeTimeMoment.isAfter(Instant.now())) {
                    notificationService.schedule(
                        scheduleId = plannedIntakeId,
                        timeMillis = intakeTimeMoment.toEpochMilli(),
                        title = currentState.name,
                        dose = currentState.dose,
                    )
                }
            }

            // region ver2
            /*            val intakeMoment =
                    DateTimeUtils.fromDateTimeValues(
                        currentPointerDate,
                        currentState.intakeTime.first,
                        currentState.intakeTime.second,
                    )

            // 1. Сохраняем прием в БД.
            medRegRepository.addSchedule(
                ScheduledDateTimeDomain(
                    id = scheduleId,
                    medicationRegimentId = medRegId,
                    scheduledIntakeDateTime = intakeMoment,
                )
            )

            // 2. Планируем уведомление (только если время еще не прошло)
            if (intakeMoment.isAfter(Instant.now())) {
                notificationService.schedule(
                    scheduleId = scheduleId,
                    timeMillis = intakeMoment.toEpochMilli(),
                    title = currentState.name,
                    dose = currentState.dose,
                )
            }*/
            // endregion
            currentPointerDate = currentPointerDate.plusDays(1)
        }

        // TODO: После создания всех записей в БД можно запланировать уведомления
        // scheduleNotification.planNext(medId)
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
}
