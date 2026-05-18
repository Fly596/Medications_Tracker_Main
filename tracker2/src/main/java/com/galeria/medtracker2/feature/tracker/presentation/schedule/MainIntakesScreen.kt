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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.ui.theme.SpeechRecognitionAppTheme
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import java.time.Instant
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
                title = { Text("Nearest intakes", style = MaterialTheme.typography.displaySmall) }
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
                IntakeList(intakes = state.todaysIntakes, onCheck = viewModel::checkIntake)
            }
            //            if (state.plannedIntakes.isEmpty() && !state.isLoading) {
            //                EmptySchedulePlaceholder()
            //            } else {
            //                IntakeList(state.plannedIntakes, onCheck = viewModel::checkIntake)
            //            }

            // Полный список приемов.

        }
    }
}

@Composable
fun IntakeList(
    intakes: List<ScheduledIntakeDetails>,
    onCheck: (Boolean, ScheduledIntakeDetails, Instant) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items = intakes, key = { it.plannedIntakeId }) { intake ->
            IntakeCard(intake, onCheck)
        }
    }
}

@Composable
fun IntakeCard(
    intake: ScheduledIntakeDetails,
    onCheck: (Boolean, ScheduledIntakeDetails, Instant) -> Unit,
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    // Кешируем отформатированное время, чтобы не перечитывать при каждом рекомпозе.
    val formattedTime =
        remember(intake.scheduledTimestamp) {
            DateTimeUtils.fromTimestampToLocalDateTime(intake.scheduledTimestamp)
                .format(DateTimeUtils.dateTimeFormatter)
        }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { isDialogVisible = true },
        colors = CardDefaults.cardColors(containerColor = MedTrackerTheme.colors.secondaryBackground)

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
        confirmButton = { Button(onClick = { onConfirm(true, Instant.now()) }) { Text("Taken") } },
        dismissButton = {
            TextButton(onClick = { onConfirm(false, Instant.now()) }) {
                Text("Skip", color = MaterialTheme.colorScheme.error)
            }
        },
    )

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
}

@Composable
private fun EmptySchedulePlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("На сегодня приемов нет", style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpeechRecognitionAppTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
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
        }
    }
}
