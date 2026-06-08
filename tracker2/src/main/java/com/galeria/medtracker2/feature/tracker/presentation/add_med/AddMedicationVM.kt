package com.galeria.medtracker2.feature.tracker.presentation.add_med

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.feature.tracker.domain.CreateMedicationsAndScheduleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

data class AddMedUiState(
    val name: String = "Adderall",
    val dose: String = "50",
    val startDateMillis: Long = DateTimeUtils.fromLocalDateToLong(LocalDate.now()),
    val endDateMillis: Long = DateTimeUtils.fromLocalDateToLong(LocalDate.now()),
    val intakeTimes: List<LocalTime> = emptyList(), // Вернули нормальное имя
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

const val TAG: String = "MyActivity"
const val DEFAULT_SCHEDULE_DAYS: Long = 7

@HiltViewModel
class AddMedicationVM
@Inject
constructor(private val createMedicationsAndScheduleUseCase: CreateMedicationsAndScheduleUseCase) :
    ViewModel() {

    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    fun updateName(input: String) = _state.update { it.copy(name = input) }

    fun updateDose(input: String) {
        // digits only.
        if (input.all { char -> char.isDigit() }) {
            _state.update { it.copy(dose = input) }
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
        val start = currentState.startDateMillis // utc
        val end = currentState.endDateMillis // utc
        val intakesTimes = currentState.intakeTimes

        // Базовая валидация (в проде нужно подсвечивать красным поля в UI)
        // region values check
        if (name.isBlank()) {
            _state.update { it.copy(errorMessage = "Name cannot be empty!") }
            return
        }

        if (dosage == null) {
            _state.update { it.copy(errorMessage = "Dosage cannot be empty!") }
            return
        }

        if (dosage <= 0) {
            _state.update { it.copy(errorMessage = "Dosage should be more than 0!") }
            return
        }

        if (intakesTimes.isEmpty()) {
            _state.update { it.copy(errorMessage = "Select intakes times!") }
            return
        }
        // endregion

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                // Добавление новых данных в БД: лекарство, график расписания
                // и запланированные приемы по часам.
                createMedicationsAndScheduleUseCase(
                    name = name,
                    dose = dosage,
                    startDate = start,
                    endDate = end,
                    intakeTimes = intakesTimes,
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
}
