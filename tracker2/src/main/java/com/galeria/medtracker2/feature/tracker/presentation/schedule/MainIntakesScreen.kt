package com.galeria.medtracker2.feature.tracker.presentation.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.components.TimePickerDialogNew
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.colors
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.shapes
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.typography
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun MainIntakesScreen(viewModel: MainIntakesVM = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Today's intakes", style = typography.display3Emphasized) })
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
              .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.todayIntakes.isEmpty()) {
                AEmptySchedulePlaceholder()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(items = state.todayIntakes, key = { intake -> intake.plannedIntakeId }) {
                        intake ->
                        AIntakeCard(
                            intake = intake,
                            // id получаем раньше, ниже передаем только статус и время.
                            onCheck = { status, time ->
                                viewModel.checkIntake(status, intake.plannedIntakeId, time)
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AIntakeCard(
    intake: ScheduledIntakeDetails,
    onCheck: (status: Boolean, intakeTime: Long) -> Unit,
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    // Кешируем отформатированное время, чтобы не перечитывать при каждом рекомпозе.
    val formattedTime =
        remember(intake.scheduledTimestamp) {
            DateTimeUtils.formatLongToLocalTimeString(intake.scheduledTimestamp)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { isDialogVisible = true },
        colors = CardDefaults.cardColors(containerColor = colors.secondaryBackground),
    ) {
        Row(
            modifier = Modifier
              .padding(16.dp)
              .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = intake.medicationName, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "$formattedTime • ${intake.doseMg} mg",
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            // Визуальный индикатор статуса
            StatusBadge(status = intake.isTaken)
        }
    }
    if (isDialogVisible) {
        CheckIntakeDialogTemp(
            medName = intake.medicationName,
            doseMg = intake.doseMg,
            onConfirm = { status, timestamp ->
                onCheck(status, timestamp)
                isDialogVisible = false
            },
          onSkip = { status, timestamp ->
            onCheck(status, timestamp)
            isDialogVisible = false
          },
          onDismiss = { isDialogVisible = false },
        )
    }
}

@Composable
fun CheckIntakeDialogTemp(
    medName: String,
    doseMg: Double,
    date: Long = Instant.now().toEpochMilli(),
    time: LocalTime = LocalTime.now(),
    onConfirm: (Boolean, Long) -> Unit,
    onSkip: (Boolean, Long) -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp), shape = RoundedCornerShape(16.dp)) {
            Column(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "$medName $doseMg mg", style = typography.title1Emphasized)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardColors(
                            containerColor = colors.secondaryBackgroundGrouped,
                            contentColor = colors.primaryLabel,
                            disabledContainerColor = colors.primaryBackground,
                            disabledContentColor = colors.primaryBackground,
                        ),
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        DateTimeRow(
                            label = "Intake time:",
                            dateText = DateTimeUtils.formatLongToLocalDateString(date),
                            timeText = DateTimeUtils.formatLocalTime(time),
                            onDateClick = { /*TODO: implement date change */ },
                            onTimeClick = { /*TODO: implement time change */ },
                        )
                        HorizontalDivider(
                            color = colors.separator,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Button(
                                onClick = {
                                    onConfirm(
                                        true,
                                        DateTimeUtils.combineDateAndTime(
                                                DateTimeUtils.fromLongToLocalDate(date),
                                                time,
                                            )
                                            .toEpochMilli(),
                                    )
                                },
                                shape = shapes.small,
                                modifier = Modifier.weight(1f),
                                colors =
                                    ButtonDefaults.buttonColors(
                                        containerColor = colors.primary400,
                                        contentColor = colors.sysWhite,
                                    ),
                            ) {
                                Text("Confirm")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            FilledTonalButton(
                              onClick = {
                                onSkip(
                                  false,
                                  DateTimeUtils.combineDateAndTime(
                                    DateTimeUtils.fromLongToLocalDate(date),
                                    time,
                                  )
                                    .toEpochMilli(),
                                )
                              },
                                shape = shapes.small,
                                modifier = Modifier.weight(0.7f),
                                colors =
                                    ButtonDefaults.filledTonalButtonColors(
                                        containerColor = Color(0x33000000),
                                        contentColor = colors.secondaryLabel,
                                    ),
                            ) {
                                Text("Skip")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AEmptySchedulePlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("На сегодня приемов нет", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun DateTimeRow(
    label: String,
    dateText: String,
    timeText: String,
    onDateClick: (Long) -> Unit,
    onTimeClick: (LocalTime) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isTimePickerVisible by rememberSaveable { mutableStateOf(false) }
    var isDatePickerVisible by rememberSaveable { mutableStateOf(false) }

    Row(
        modifier = modifier
          .fillMaxWidth()
          .height(48.dp)
          .padding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, color = colors.primaryLabel, style = typography.bodyMedium)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PickerPill(text = dateText, onClick = { isDatePickerVisible = true })
            PickerPill(text = timeText, onClick = { isTimePickerVisible = true })
        }
    }
    if (isDatePickerVisible) {
        DatePickerModalNew(
            onDateSelected = { selectedDateLong ->
                if (selectedDateLong != null) {
                    onDateClick(selectedDateLong)
                }
                isDatePickerVisible = false
            },
            onDismiss = { isDatePickerVisible = false },
        )
    }

    if (isTimePickerVisible) {
        TimePickerDialogNew(
            onConfirm = { time ->
                onTimeClick(time)
                isTimePickerVisible = false
            },
            onDismiss = { isTimePickerVisible = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalNew(onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit) {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = Instant.now().toEpochMilli())

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
                Text("OK")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } },
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun PickerPill(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        color = colors.secondaryBackground, // Контрастный серый цвет для кнопок-пилюль
        contentColor = colors.primaryLabel,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = text, style = typography.labelSmall)
        }
    }
}

@Composable
fun StatusBadge(status: Boolean?) {
    val (text, color) =
        when (status) {
            true -> "Taken" to colors.sysSuccess
            false -> "Missed" to MedTrackerTheme.colors.sysError
            null -> "Await" to MedTrackerTheme.colors.secondaryLabel
        }

    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelLarge,
            color = color,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0x00FFFFFF, showSystemUi = false)
@Composable
fun GreetingPreviewAMN() {
    val date = LocalDate.of(2026, 6, 15).toEpochDay()
    val time = LocalTime.of(12, 30)

    MedTrackerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            /*     CheckIntakeDialogTemp(
                     "Adderall",
                     13.0,
                     date,
                     time,
                 )*/
        }
    }
}
