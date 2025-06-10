package com.galeria.medicationstracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.data.NewMedicationRepository
import com.galeria.medicationstracker.utils.convertMillisToDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// Data class representing the UI state for the temperature screen
data class TempUiState(
    val startDate: Long? = null, // Start date in milliseconds
    val endDate: Long? = null, // End date in milliseconds
    val showPicker: Boolean = false, // Flag to show/hide date picker
)

// ViewModel for handling temperature screen logic
@HiltViewModel
class TempVM @Inject constructor(private val medicationRepository: NewMedicationRepository) :
    ViewModel() {
    
    // Mutable state flow for UI state
    private val _uiState = MutableStateFlow(TempUiState())
    
    // Public immutable state flow
    val uiState = _uiState.asStateFlow()
    
    // Handle date selection events
    fun onDatesSelected(start: Long?, end: Long?) {
        _uiState.update { currentState ->
            currentState.copy(
                startDate = start,
                endDate = end
            )
        }
    }
}

// Composable function for the medication data screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestMedicationDataScreen(viewModel: TempVM = hiltViewModel()) {
    // Collect UI state from ViewModel
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    // Local state for date picker visibility
    var showPicker by rememberSaveable { mutableStateOf(false) }
    // State for date range picker
    val dateRangePickerState = rememberDateRangePickerState()
    
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        // Button to toggle date picker visibility
        Button(onClick = { showPicker = !showPicker }) { Text("Show picker") }
        // Date picker dialog
        if (showPicker) {
            DatePickerDialog(
                onDismissRequest = { showPicker = !showPicker },
                confirmButton = {
                    Button(
                        onClick = {
                            // Update selected dates in ViewModel
                            viewModel.onDatesSelected(
                                dateRangePickerState.selectedStartDateMillis,
                                dateRangePickerState.selectedEndDateMillis,
                            )
                            showPicker = !showPicker
                        }
                    ) {
                        Text("Confirm")
                    }
                },
            ) {
                DateRangePicker(state = dateRangePickerState)
            }
        }
        // Display selected dates
        Text(text = convertMillisToDate(state.value.startDate))
        Text(text = convertMillisToDate(state.value.endDate))
    }
}

sealed interface Date
