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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.colors
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.typography
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import java.time.Instant
import java.time.LocalTime
import java.util.UUID

@Composable
fun MainIntakesScreen(
    onNavigateToAddMedication: () -> Unit = {},
    onNavigateToMedicationsList: () -> Unit = {},
    viewModel: MainIntakesVM = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Nearest intakes", style = MaterialTheme.typography.displaySmall)
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Button(
                onClick = onNavigateToAddMedication,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("On add med page")
            }
            Button(
                onClick = onNavigateToMedicationsList,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("On medications list page")
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.todaysIntakes.isEmpty()) {
                EmptySchedulePlaceholder()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = state.todaysIntakes,
                        key = { intake
                            ->
                            intake.plannedIntakeId
                        }
                    ) { intake ->
                        IntakeCardRew(intake, onCheck = { status, plannedIntakeId, intakeTime ->
                            viewModel.checkIntakeRef(status, plannedIntakeId, intakeTime)
                        })
                        IntakeCard(intake, viewModel::checkIntake)
                    }
                }
            }

        }
    }
}

@Composable
fun IntakeCardRew(
    intake: ScheduledIntakeDetails,
    onCheck: (status: Boolean, plannedIntakeId: UUID, intakeTime: Instant) -> Unit,
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    // Кешируем отформатированное время, чтобы не перечитывать при каждом рекомпозе.
    val formattedTime =
        remember(intake.scheduledTimestamp) {
            DateTimeUtils.formatLongToLocalDateTimeString(intake.scheduledTimestamp)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { isDialogVisible = true },
        colors =
            CardDefaults.cardColors(containerColor = colors.secondaryBackground),
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
            onConfirm = { onCheck } // TODO: WTF
        )
        /* CheckIntakeDialog(
             intake = intake,
             onConfirm = { isTaken, time ->
                 onCheck(isTaken, intake, time)
                 isDialogVisible = false
             },
             onDismiss = { isDialogVisible = false },
         )*/
    }
}

@Composable
fun IntakeCard(
    intake: ScheduledIntakeDetails,
    onCheck: (status: Boolean, ScheduledIntakeDetails, Instant) -> Unit,
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    // Кешируем отформатированное время, чтобы не перечитывать при каждом рекомпозе.
    val formattedTime =
        remember(intake.scheduledTimestamp) {
            DateTimeUtils.formatLongToLocalDateTimeString(intake.scheduledTimestamp)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { isDialogVisible = true },
        colors =
            CardDefaults.cardColors(containerColor = colors.secondaryBackground),
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
        CheckIntakeDialog(
            intake = intake,
            onConfirm = { isTaken, time ->
                onCheck(isTaken, intake, time)
                isDialogVisible = false
            },
            onDismiss = { isDialogVisible = false },
        )
    }

    // region old

//     @Composable
// fun IntakeList(
//     intakes: List<ScheduledIntakeDetails>,
//     onCheck: (Boolean, ScheduledIntakeDetails, Instant) -> Unit,
// ) {
//     LazyColumn(
//         modifier = Modifier.fillMaxSize(),
//         contentPadding = PaddingValues(bottom = 16.dp),
//         verticalArrangement = Arrangement.spacedBy(16.dp),
//     ) {
//         items(items = intakes, key = { it.plannedIntakeId }) { intake ->
//             IntakeCard(intake, onCheck)
//         }
//     }
// }
    //    if (isDialogVisible) {
    //        CheckIntakeDialog(
    //            intake = intake,
    //            onCheck = onCheck,
    //            onDismissRequest = { isDialogVisible = false },
    //        )
    //    } else {
    //        Card(
    //            modifier = Modifier.fillMaxWidth(),
    //            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    //        ) {
    //            Column(modifier = Modifier.padding(16.dp)) {
    //                Row(
    //                    modifier = Modifier.fillMaxWidth(),
    //                    horizontalArrangement = Arrangement.SpaceBetween,
    //                    verticalAlignment = Alignment.CenterVertically,
    //                ) {
    //                    Text(text = intake.medicationName, style =
    // MaterialTheme.typography.titleLarge)
    //                    Text(
    //                        text = "${intake.doseMg} mg",
    //                        style = MaterialTheme.typography.bodyLarge,
    //                        color = MaterialTheme.colorScheme.primary,
    //                    )
    //                }
    //
    //                Spacer(modifier = Modifier.height(4.dp))
    //
    //                Row(
    //                    modifier = Modifier.fillMaxWidth(),
    //                    horizontalArrangement = Arrangement.SpaceBetween,
    //                    verticalAlignment = Alignment.Bottom,
    //                ) {
    //                    Text(
    //                        text = formattedTime,
    //                        style = MaterialTheme.typography.bodyMedium,
    //                        color = MaterialTheme.colorScheme.onSurfaceVariant,
    //                    )
    //                    val statusText =
    //                        when (intake.isTaken) {
    //                            null -> "No Status"
    //                            true -> "Taken"
    //                            false -> "Missed"
    //                        }
    //                    Text(
    //                        text = statusText,
    //                        style = MaterialTheme.typography.bodyMedium,
    //                    )
    //                    // TODO: функционал отметки приема.
    //                    Button(
    //                        onClick = {
    //                            // open intake dialog.
    //                            isDialogVisible = !isDialogVisible
    //                        },
    //                        shape = RoundedCornerShape(percent = 100),
    //                    ) {
    //                        Icon(imageVector = Icons.Default.Add, contentDescription = "check
    // intake")
    //                    }
    //                }
    //            }
    //        }
    //    }
    // endregion
}

@Composable
fun StatusBadge(status: Boolean?) {
    val (text, color) =
        when (status) {
            true -> "Taken" to Color(0xFF4CAF50)
            false -> "Missed" to MaterialTheme.colorScheme.error
            null -> "Await" to MaterialTheme.colorScheme.outline
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

@Composable
fun CheckIntakeDialog(
    intake: ScheduledIntakeDetails,
    onConfirm: (Boolean, Instant) -> Unit,
    onDismiss: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Check intake") },
        text = { Text("You took ${intake.medicationName} (${intake.doseMg} mg)?") },
        confirmButton = {
            Button(onClick = { onConfirm(true, Instant.now()) }) {
                Text("Taken"
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { onConfirm(false, Instant.now()) }) {
                Text("Skip", color = MaterialTheme.colorScheme.error)
            }
        },
    )
    /*
        //    Dialog(onDismissRequest = { onDismiss() }) {
        //        Card(
        //            shape = MaterialTheme.shapes.medium,
        //            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        //        ) {
        //            Column(modifier = Modifier.padding(16.dp)) {
        //                Row(
        //                    modifier = Modifier.fillMaxWidth(),
        //                    horizontalArrangement = Arrangement.SpaceBetween,
        //                    verticalAlignment = Alignment.CenterVertically,
        //                ) {
        //                    Text(text = intake.medicationName, style =
        // MaterialTheme.typography.titleLarge)
        //                    Text(
        //                        text = "${intake.doseMg} mg",
        //                        style = MaterialTheme.typography.bodyLarge,
        //                        color = MaterialTheme.colorScheme.primary,
        //                    )
        //                }
        //                Row(
        //                    modifier = Modifier.fillMaxWidth(),
        //                    horizontalArrangement = Arrangement.SpaceEvenly,
        //                    verticalAlignment = Alignment.Bottom,
        //                ) {
        //                    Button(
        //                        onClick = {
        //                            // TODO: choose time.
        //                            onConfirm(true, Instant.now())
        //                            onDismiss()
        //                        }
        //                    ) {
        //                        Text("Confirm")
        //                    }
        //                    Button(
        //                        onClick = {
        //                            onCheck(false, intake, Instant.now())
        //                            onDismiss()
        //                        }
        //                    ) {
        //                        Text("Skip")
        //                    }
        //                }
        //            }
        //        }
        //    }
        */
}

@Composable
private fun EmptySchedulePlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("На сегодня приемов нет", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun CheckIntakeDialogTemp(
    //intake: ScheduledIntakeDetails,
    medName: String,
    doseMg: Double,
    date: Long = Instant.now().toEpochMilli(),
    time: LocalTime = LocalTime.now(),
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Check your dose", style = typography.title1Emphasized)
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(), colors = CardColors(
                        containerColor = colors.secondaryBackgroundGrouped,
                        contentColor = colors.primaryLabel,
                        disabledContainerColor = colors.primaryBackground,
                        disabledContentColor = colors.primaryBackground,
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Column(
                            modifier = Modifier.height(48.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = medName,
                                style = typography.bodyLargeEmphasized
                            )
                            Text(text = "$doseMg mg", style = typography.bodyMedium)

                        }

                        HorizontalDivider(
                            color = colors.separator,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )

                        DateTimeRow(
                            label = "Intake time:",
                            dateText = DateTimeUtils.formatLongToLocalDateString(date),
                            timeText = DateTimeUtils.formatLocalTime(time),
                            onDateClick = {},
                            onTimeClick = {}
                        )
                    }

                }

            }
        }
    }

    Surface(color = colors.primaryBackgroundGrouped, modifier = Modifier) {

    }
}

@Composable
private fun DateTimeRow(
    label: String,
    dateText: String,
    timeText: String,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            color = colors.primaryLabel,
            style = typography.bodyLargeEmphasized
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PickerPill(text = dateText, onClick = onDateClick)
            PickerPill(text = timeText, onClick = onTimeClick)
        }
    }
}

@Composable
private fun PickerPill(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = colors.primaryFill, // Контрастный серый цвет для кнопок-пилюль
        contentColor = colors.primaryLabel,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = typography.labelLarge
            )
        }
    }
}

@Preview(
    showBackground = true, backgroundColor = 0x00FFFFFF, showSystemUi = false
)
@Composable
fun GreetingPreview() {

    MedTrackerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            CheckIntakeDialogTemp(
                medName = "name",
                14.0
            )

        }
        /*LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(4) {
                IntakeCard(
                    intake =
                        ScheduledIntakeDetails(
                            plannedIntakeId = UUID.randomUUID(),
                            courseId = UUID.randomUUID(),
                            medicationName = "Name",
                            doseMg = 56.0,
                            scheduledTimestamp = 0,
                            isTaken = null,
                        ),
                    onCheck = { _, _, _ -> },
                )
            }
        }*/
    }
}
