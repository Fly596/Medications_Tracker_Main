package com.galeria.medicationstracker.feature_auth.presentation.registration

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel = hiltViewModel()) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        OutlinedTextField(
            value =
                viewModel.convertMilliisToStringDate(state.value.birthDate.toEpochMilliseconds()),
            onValueChange = {},
            label = { Text("DOB") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { viewModel.showDatePicker() }) {
                    Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select date")
                }
            },
            modifier = Modifier.fillMaxWidth().height(64.dp),
        )
        if (showDatePicker) {
            ModalDatePicker(
                datePickerState = datePickerState,
                onDateSelected = {
                    it?.let { selectedDate = viewModel.convertMilliisToStringDate(it) }
                    viewModel.updateBirthDate(it)
                },
                onDismiss = { viewModel.dismissDatePicker() },
            )
        }
    }
}

@Composable
fun ModalDatePicker(
    modifier: Modifier = Modifier,
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    ) {
        DatePicker(state = datePickerState)
    }
}
