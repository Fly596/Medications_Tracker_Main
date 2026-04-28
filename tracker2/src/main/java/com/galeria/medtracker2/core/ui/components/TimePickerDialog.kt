package com.galeria.medtracker2.core.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
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
import java.util.Calendar

@Composable
fun TimeSelectionRow(
    label: String = "Time",
    selectedTimeString: String,
    onTimeSelected: (Pair<Int, Int>) -> Unit,
    onValueChange: (String) -> Unit = {},
) {
    // Состояние для времени (часы и минуты).
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val selectedTime = remember { mutableStateOf(selectedTimeString) }

    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextField(
            value = selectedTimeString,
            label = { Text(label) },
            onValueChange = { selectedTime.value = it },
            singleLine = true,
            readOnly = true,
            modifier = Modifier, /*.weight(1f)*/
            interactionSource =
                    remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showTimePicker = true
                                    }
                                }
                            }
                        },
        )
        // Отображение диалогов.
        if (showTimePicker) {
            TimePickerDialog(
                onConfirm = { hour, minute ->
                    onTimeSelected(Pair(hour, minute))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false },
            )
        }
    }
}

@Composable
fun TimeSelectionButton(
    label: String = "Time",
    selectedTimeString: String,
    onTimeSelected: (Pair<Int, Int>) -> Unit,
    onValueChange: (String) -> Unit = {},
) {
    // Состояние для времени (часы и минуты).
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val selectedTime = remember { mutableStateOf(selectedTimeString) }

    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            modifier = Modifier,
            onClick = { selectedTime.value = selectedTimeString },
            interactionSource =
                    remember { MutableInteractionSource() }
                        .also { interactionSource ->
                            LaunchedEffect(interactionSource) {
                                interactionSource.interactions.collect {
                                    if (it is PressInteraction.Release) {
                                        showTimePicker = true
                                    }
                                }
                            }
                        },
        ) {
            Text(label)
        }

        // Отображение диалогов.
        if (showTimePicker) {
            TimePickerDialog(
                onConfirm = { hour, minute ->
                    onTimeSelected(Pair(hour, minute))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()
    val timePickerState =
            rememberTimePickerState(
                initialHour = currentTime.get(Calendar.HOUR),
                initialMinute = currentTime.get(Calendar.MINUTE),
                is24Hour = false,
            )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState.hour, timePickerState.minute) }) {
                Text("Confirm")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Dismiss") } },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TimeInput(state = timePickerState)
            }
        },
    )
}
