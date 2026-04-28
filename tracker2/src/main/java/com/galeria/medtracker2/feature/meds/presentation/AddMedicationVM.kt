package com.galeria.medtracker2.feature.meds.presentation

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.notification.ScheduleNotification
import com.galeria.medtracker2.feature.meds.domain.MedicationCourseDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

data class AddMedUiState(
    val name: String = "test",
    val dose: String = "56",
    val startDate: Long = 0,
    val endDate: Long? = null,
    val intakeTime: Pair<Int, Int> = Pair(0, 0),
    val intakeTimes: List<Pair<Int, Int>> = emptyList(),
    val startDateString: String = "Choose date",
    val endDateString: String = "Choose date",
    val intakeTimeString: String = "Выберите время",
    val isStartDatePickerVisible: Boolean = false,
    val isEndDatePickerVisible: Boolean = false,
    val isTimePickerVisible: Boolean = false,
)

const val TAG: String = "MyActivity"
@HiltViewModel
class AddMedicationVM
@Inject
constructor(
    private val repository: MedsRepository,
    private val medRegRepository: MedicationRegimenRepo,
    private val notificationService: ScheduleNotification,
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    // region ui update.
    fun updateName(input: String) = _state.update { it.copy(name = input) }

    fun updateDose(input: String) = _state.update { it.copy(dose = input) }

    fun updateStartDate(input: Long) {
        val date = DateTimeUtils.fromTimestampToLocalDate(input)
        _state.update {
            it.copy(startDateString = date.format(DateTimeUtils.dateFormatter), startDate = input)
        }
    }

    fun updateEndDate(input: Long?) {
        input?.let {
            val date = DateTimeUtils.fromTimestampToLocalDate(it)
            _state.update {
                it.copy(endDateString = date.format(DateTimeUtils.dateFormatter), endDate = input)
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
                    MedicationDomain(
                        medicationId,
                        currentState.name,
                        Instant.now()
                    )
                )

                // 2. Генерируем дни приема и ставим алармы
                generateScheduleEntries(medicationId, currentState)
            } catch (e: Exception) {
                // В продакшене здесь должен быть вывод ошибки пользователю (через SideEffect)
                Log.e("AddMedicationVM", "Error saving medication", e)
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

    private suspend fun generateScheduleEntries(medicationId: UUID, currentState: AddMedUiState) {
        val start = DateTimeUtils.fromTimestampToLocalDate(currentState.startDate)
        val end =
                DateTimeUtils.fromTimestampToLocalDate(
                    currentState.endDate ?: currentState.startDate
                )

        // Считаем кол-во дней.
        val daysCount = ChronoUnit.DAYS.between(start, end).toInt()
        val medicationCourseId = UUID.randomUUID()

        // Сохраняем общ инфу о курсе (режим приема).
        medRegRepository.addRegiment(
            MedicationCourseDomain(
                id = medicationCourseId,
                medicationId = medicationId,
                doseMg = currentState.dose.toDoubleOrNull() ?: 0.0,
                startDate = Instant.ofEpochMilli(currentState.startDate),
                endDate = Instant.ofEpochMilli(currentState.endDate ?: currentState.startDate),
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
                medRegRepository.addSchedule(
                    PlannedIntakeDomain(
                        id = plannedIntakeId,
                        medicationCourseId = medicationCourseId,
                        scheduledIntakeDateTime = intakeTimeMoment,
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
}
