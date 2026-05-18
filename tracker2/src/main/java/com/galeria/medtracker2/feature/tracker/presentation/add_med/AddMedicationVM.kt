package com.galeria.medtracker2.feature.tracker.presentation.add_med

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.notifications.data.ScheduleNotificationRepoImpl
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.MedicationCourseDomain
import com.galeria.medtracker2.domain.model.MedicationDomain
import com.galeria.medtracker2.domain.model.PlannedIntakeDomain
import com.galeria.medtracker2.domain.repository.MedicationRepository
import com.galeria.medtracker2.domain.repository.MedicationScheduleIntakesRepository
import com.galeria.medtracker2.feature.tracker.domain.CreateMedicationsAndScheduleUseCase
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
    val startDateMillis: Long = 0,
    val endDateMillis: Long = 0,
    val intakeTimes: List<LocalTime> = emptyList(), // Вернули нормальное имя
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
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
    private val createMedicationsAndScheduleUseCase: CreateMedicationsAndScheduleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    fun updateName(input: String) = _state.update { it.copy(name = input) }

    fun updateDose(input: String) {
        // digits only.
        if (input.all { char ->
                char.isDigit()
            }
        ) {
            _state.update {
                it.copy(dose = input)
            }
        }
    }

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
        val name = currentState.name.trim()
        val dosage = currentState.dose.toDoubleOrNull()
        val start = currentState.startDateMillis
        val end = currentState.endDateMillis
        val intakesTimes = currentState.intakeTimes

        // Базовая валидация (в проде нужно подсвечивать красным поля в UI)

        // region values check
        if (name.isBlank()) {
            _state.update {
                it.copy(
                    errorMessage = "Name cannot be empty!"
                )
            }
            return
        }

        if (dosage==null) {
            _state.update {
                it.copy(
                    errorMessage = "Dosage cannot be empty!"
                )
            }
            return
        }

        if (dosage <= 0) {
            _state.update {
                it.copy(
                    errorMessage = "Dosage should be more than 0!"
                )
            }
            return
        }

        if (intakesTimes.isEmpty()) {
            _state.update {
                it.copy(
                    errorMessage = "Select intakes times!"
                )
            }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                createMedicationsAndScheduleUseCase(
                    name = name,
                    dose = dosage,
                    startDate = start,
                    endDate = end,
                    intakeTimes = intakesTimes
                )
                // TODO: ui ивент для навигации назад.
            } catch (e: Exception) {
                // Ловим ошибки.
                _state.update { it.copy(errorMessage = e.localizedMessage) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }

    }

    fun OldaddMedication() {
        val currentState = _state.value
        val name = currentState.name.trim()
        val dosage = currentState.dose.toDoubleOrNull()
        val start = currentState.startDateMillis
        val end = currentState.endDateMillis

        // Базовая валидация (в проде нужно подсвечивать красным поля в UI)

        // region values check
        if (name.isBlank()) {
            _state.update {
                it.copy(
                    errorMessage = "Name cannot be empty!"
                )
            }
            return
        }

        if (dosage==null) {
            _state.update {
                it.copy(
                    errorMessage = "Dosage cannot be empty!"
                )
            }
            return
        }

        if (dosage <= 0) {
            _state.update {
                it.copy(
                    errorMessage = "Dosage should be more than 0!"
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                val medicationId = UUID.randomUUID()
                repository.addMedication(
                    MedicationDomain(medicationId, currentState.name, Instant.now())
                )
                generateScheduleEntries(medicationId)
            } catch (e: Exception) {
                Log.e(TAG, "Error saving medication", e)
            }
        }
    }

    private suspend fun generateScheduleEntries(
        medicationId: UUID,
    ) {
        val today = LocalDate.now()

        // 1. Получаем стартовую дату (LocalDate). Если не выбрана — берем сегодня.
        // Используем строго UTC конвертер, так как millis пришли из DatePicker!
        val rawStartDate =
            _state.value.startDateMillis?.let { DateTimeUtils.fromLongToLocalDate(it) }
                ?: today

        // Не даем создавать расписание в прошлом. Если выбрали вчера, начинаем с сегодня.
        val start = if (rawStartDate.isBefore(today)) today else rawStartDate

        // 2. Получаем конечную дату. Если не выбрана, прибавляем дни к start.
        val end =
            _state.value.endDateMillis?.let { DateTimeUtils.fromLongToLocalDate(it) }
                ?: start.plusDays(DEFAULT_SCHEDULE_DAYS)

        val daysCount = ChronoUnit.DAYS.between(start, end).toInt()
        val medicationCourseId = UUID.randomUUID()

        // 3. Сохраняем курс.
        medRegRepository.addCourse(
            MedicationCourseDomain(
                id = medicationCourseId,
                medicationId = medicationId,
                doseMg = _state.value.dose.toDoubleOrNull() ?: 0.0,
                startDate = start.atStartOfDay(ZoneId.systemDefault()).toInstant(),
                endDate = end.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant(),
            )
        )
        // 4. Генерируем приемы по дням
        var currentPointerDate = start
        for (i in 0..daysCount) {
            _state.value.intakeTimes.forEach { localTime ->
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
                        title = _state.value.name,
                        dose = _state.value.dose,
                    )
                }
            }
            currentPointerDate = currentPointerDate.plusDays(1)
        }
    }
}
