package com.galeria.medtracker2.feature.meds.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.notification.data.ScheduleNotificationRepoImpl
import com.galeria.medtracker2.feature.meds.domain.MedicationCourseDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationDomain
import com.galeria.medtracker2.feature.meds.domain.MedicationRepository
import com.galeria.medtracker2.feature.meds.domain.MedicationScheduleIntakesRepository
import com.galeria.medtracker2.feature.meds.domain.PlannedIntakeDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

data class AddMedUiState(
    val name: String = "",
    val dose: String = "",
    val startDateMillis: Long? = null,
    val endDateMillis: Long? = null,
    val intakeTimes: List<LocalTime> = emptyList(), // Вернули нормальное имя
)

const val TAG: String = "MyActivity"
const val DEFAULT_SCHEDULE_DAYS: Long = 7

@HiltViewModel
class AddMedicationVM
@Inject
constructor(
    private val repository: MedicationRepository,
    private val medRegRepository: MedicationScheduleIntakesRepository,
    private val notificationService: ScheduleNotificationRepoImpl,
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    fun updateName(input: String) = _state.update { it.copy(name = input) }

    fun updateDose(input: String) = _state.update { it.copy(dose = input) }

    fun updateStartDate(millis: Long) = _state.update { it.copy(startDateMillis = millis) }

    fun updateEndDate(millis: Long) = _state.update { it.copy(endDateMillis = millis) }

    fun addTime(time: LocalTime) {
        if (time !in _state.value.intakeTimes) {
            // Сортируем время по порядку (утром -> вечером), это улучшает UX
            val newList = (_state.value.intakeTimes + time).sorted()
            _state.update { it.copy(intakeTimes = newList) }
        }
    }

    fun removeTime(time: LocalTime) {
        _state.update { it.copy(intakeTimes = it.intakeTimes - time) }
    }

    fun addMedication() {
        val currentState = _state.value
        // Базовая валидация (в проде нужно подсвечивать красным поля в UI)
        if (currentState.name.isBlank() || currentState.intakeTimes.isEmpty()) return

        viewModelScope.launch {
            try {
                val medicationId = UUID.randomUUID()
                repository.addMedication(
                    MedicationDomain(medicationId, currentState.name, Instant.now())
                )
                generateScheduleEntries(medicationId, currentState)
            } catch (e: Exception) {
                Log.e(TAG, "Error saving medication", e)
            }
        }
    }

    private suspend fun generateScheduleEntries(
        medicationId: UUID,
        currentState: AddMedUiState,
    ) {
        val today = LocalDate.now()

        // 1. Получаем стартовую дату (LocalDate). Если не выбрана — берем сегодня.
        // Используем строго UTC конвертер, так как millis пришли из DatePicker!
        val rawStartDate =
            currentState.startDateMillis?.let { DateTimeUtils.fromDatePickerMillisToLocalDate(it) }
                ?: today

        // Не даем создавать расписание в прошлом. Если выбрали вчера, начинаем с сегодня.
        val start = if (rawStartDate.isBefore(today)) today else rawStartDate

        // 2. Получаем конечную дату. Если не выбрана, прибавляем дни к start.
        val end =
            currentState.endDateMillis?.let { DateTimeUtils.fromDatePickerMillisToLocalDate(it) }
                ?: start.plusDays(DEFAULT_SCHEDULE_DAYS)

        val daysCount = ChronoUnit.DAYS.between(start, end).toInt()
        val medicationCourseId = UUID.randomUUID()

        // 3. Сохраняем курс.
        medRegRepository.addCourse(
            MedicationCourseDomain(
                id = medicationCourseId,
                medicationId = medicationId,
                doseMg = currentState.dose.toDoubleOrNull() ?: 0.0,
                startDate = start.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                endDate = end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant(),
            )
        )
        // 4. Генерируем приемы по дням
        var currentPointerDate = start
        for (i in 0..daysCount) {
            currentState.intakeTimes.forEach { localTime ->
                val plannedIntakeId = UUID.randomUUID()

                // Объединяем дату (PointerDate) и время (LocalTime) в нужный момент (Instant)
                val intakeTimeMoment =
                    DateTimeUtils.combineDateAndTime(currentPointerDate, localTime)

                medRegRepository.addPlannedIntake(
                    PlannedIntakeDomain(
                        id = plannedIntakeId,
                        courseId = medicationCourseId,
                        scheduledTimestamp = intakeTimeMoment,
                    )
                )
                // Планируем уведомление, если время приема еще не наступило
                if (intakeTimeMoment.isAfter(Instant.now())) {
                    notificationService.schedule(
                        scheduleId = plannedIntakeId,
                        timeMillis = intakeTimeMoment.toEpochMilli(),
                        title = currentState.name,
                        dose = currentState.dose,
                    )
                }
            }
            currentPointerDate = currentPointerDate.plusDays(1)
        }
    }
}
