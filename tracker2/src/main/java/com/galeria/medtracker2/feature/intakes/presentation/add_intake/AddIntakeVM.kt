package com.galeria.medtracker2.feature.intakes.presentation.add_intake

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.repository.IntakesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime
import javax.inject.Inject

data class IntakeTimestampState(
    val time: LocalTime = LocalTime.now(),
    val date: Long = System.currentTimeMillis()
)

data class AddIntakeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val dosage: String = "",
    val time: String = "",
    val date: String = "",
)

@HiltViewModel
class AddIntakeVM @Inject
constructor(
    private val intakeRepository: IntakesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddIntakeUiState())
    val uiState = _uiState.asStateFlow()

    fun updateDosage(input: String) {
        _uiState.update {
            it.copy(dosage = input)
        }
    }

    fun updateTime(input: LocalTime) {
        val timeString = DateTimeUtils.formatLocalTime(input)
        _uiState.update {
            it.copy(time = timeString)
        }
    }

    fun updateDate(input: Long) {
        val dateString = DateTimeUtils.formatLongToLocalDateString(input)
        _uiState.update {
            it.copy(date = dateString)
        }
    }
}