package com.galeria.medtracker2.feature.meds.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.feature.meds.domain.MedsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddMedicationScreenState(
    val name: String = "",
    val dose: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val selectedTime: String = "",
)

@HiltViewModel
class AddMedicationVM @Inject constructor(private val repository: MedsRepository) : ViewModel() {

    private val _state = MutableStateFlow(AddMedicationScreenState())
    val state = _state.asStateFlow()

    fun updateName(input: String) {
        viewModelScope.launch { _state.update { it.copy(name = input) } }
    }

    fun updateDose(input: String) {
        viewModelScope.launch { _state.update { it.copy(dose = input) } }
    }

    fun updateStartDate(input: Long) {
        viewModelScope.launch {
            val parsedDate = DateTimeUtils.fromTimestamp(input)

            val formattedDate = parsedDate.format(DateTimeUtils.dateFormatter)

            _state.update { it.copy(startDate = formattedDate) }
        }
    }

    fun updateEndDate(input: Long) {
        viewModelScope.launch {
            val parsedDate = DateTimeUtils.fromTimestamp(input)

            val formattedDate = parsedDate.format(DateTimeUtils.dateFormatter)

            _state.update { it.copy(endDate = formattedDate) }
        }
    }

    fun updateTime(time: Pair<Int, Int>) {
        val formattedTime = "%02d:%02d".format(time.first, time.second)
        _state.update { it.copy(selectedTime = formattedTime) }
    }
}
