package com.galeria.medtracker2.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DateSelectionRow(
    label: String = "Date",
    selectedDateString: String,
    onDateSelected: (Long) -> Unit,
) {
    // Состояние для даты.
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(selectedDateString) }

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //Text(text = label, style = MaterialTheme.typography.bodyMedium)
        TextField(
            value = selectedDateString,
            onValueChange = { selectedDate.value = it },
            singleLine = true,
            readOnly = true,
            label = { Text(label) },
            modifier = Modifier,/*.weight(1f)*/
            interactionSource = remember { MutableInteractionSource() }.also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            showDatePicker = true
                        }
                    }
                }
            },
        )
        // Отображение диалогов.
        if (showDatePicker) {
            DatePickerModalInput(
                onDateSelected = { selectedDate ->
                    if (selectedDate != null) {
                        onDateSelected(selectedDate)
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

