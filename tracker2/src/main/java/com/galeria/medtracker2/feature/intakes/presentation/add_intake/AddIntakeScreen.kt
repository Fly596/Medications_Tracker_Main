package com.galeria.medtracker2.feature.intakes.presentation.add_intake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.utils.DateTimeUtils
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddIntakeScreen(
    onNavigateBack: () -> Unit = {},
    onAddIntake: () -> Unit = {},
    viewModel: AddIntakeVM = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSavedSuccess) {
        if (uiState.isSavedSuccess) {
            snackbarHostState.showSnackbar("Успешно сохранено в Room как Instant!")
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New Intake",
                        style = MedTrackerTheme.typography.headlineEmphasized
                    )
                },
                colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                windowInsets =
                        WindowInsets(
                            top = 0,
                            bottom = 0,
                        ),
            )

        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.dosage,
                onValueChange = { viewModel.onEvent(AddIntakeUiEvent.OnDosageChanged(it)) },
                label = { Text("Dosage") },
                modifier = Modifier.fillMaxWidth()
            )
            // Карточка Выбора Даты (месяц-день-год)
            DateTimeCard(
                text = DateTimeUtils.formatDate(uiState.selectedDate),
                icon = Icons.Default.DateRange,
                contentDescription = "Select Date",
                onClick = { viewModel.onEvent(AddIntakeUiEvent.OpenDatePicker) }
            )

            // Карточка Выбора Времени (12h format)
            DateTimeCard(
                text = DateTimeUtils.formatTime(uiState.selectedTime),
                icon = Icons.Default.Schedule,
                contentDescription = "Select Time",
                onClick = { viewModel.onEvent(AddIntakeUiEvent.OpenTimePicker) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.onEvent(AddIntakeUiEvent.ConfirmAndSave) },
                enabled = !uiState.isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(text = "Подтвердить и Сохранить")
                }
            }
        }
    }
    if (uiState.isDatePickerOpen) {
        ScheduleDatePickerDialog(
            onDateSelected = { viewModel.onEvent(AddIntakeUiEvent.OnDateSelected(it)) },
            onDismiss = { viewModel.onEvent(AddIntakeUiEvent.DismissDatePicker) }
        )
    }

    if (uiState.isTimePickerOpen) {
        ScheduleTimePickerDialog(
            initialTime = uiState.selectedTime,
            onTimeSelected = { viewModel.onEvent(AddIntakeUiEvent.OnTimeSelected(it)) },
            onDismiss = { viewModel.onEvent(AddIntakeUiEvent.DismissTimePicker) }
        )
    }
}

@Composable
private fun DateTimeCard(
    text: String,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleDatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        onDateSelected(DateTimeUtils.parseUtcMillisToLocalDate(millis))
                    }
                }
            ) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScheduleTimePickerDialog(
    initialTime: LocalTime,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = false // Строго 12ч формат по ТЗ
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val time = DateTimeUtils.toLocalTime(
                        timePickerState.hour,
                        timePickerState.minute
                    )
                    onTimeSelected(time)
                }
            ) {
                Text("ОК")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}