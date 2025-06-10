package com.galeria.medicationstracker

import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.data.NewMedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class TempUiState(
    val startDate: Long? = null,
    val endDate: Long? = null,
    val showPicker: Boolean = false,
)

@HiltViewModel
class TempVM
@Inject
constructor(private val medicationRepository: NewMedicationRepository) :
    ViewModel() {
    
    private val _uiState = MutableStateFlow(TempUiState())
    val uiState = _uiState.asStateFlow()
    
    fun onDatesSelected(start: Long?, end: Long?) {
        _uiState.update { it.copy(startDate = start, endDate = end) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestMedicationDataScreen(viewModel: TempVM = hiltViewModel()) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    var showPicker by rememberSaveable { mutableStateOf(false) }
    val dateRangePickerState = rememberDateRangePickerState()
    
    Button(onClick = { showPicker = !showPicker }) { Text("Show picker") }
    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = { showPicker = !showPicker },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.onDatesSelected(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis,
                        )
                    }
                ) {}
            },
        ) {
            DateRangePicker(state = dateRangePickerState)
        }
    }
    Text(text = "${state.value.startDate}")
    Text(text = "${state.value.endDate}")
}

sealed interface Date
