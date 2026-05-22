package com.galeria.medtracker2.feature.tracker.presentation.schedule

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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.colors
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme.typography
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import java.util.UUID


@Composable
fun AMainIntakesScreen(
    navigateToAddMedication: () -> Unit = {},
    navigateToMedicationsList: () -> Unit = {},
    viewModel: AMainIntakesVM = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("Today's intakes", style = typography.display3Emphasized)
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
            // Временные кнопки для навигации.
            Row() {
                Button(
                    onClick = navigateToAddMedication,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text("On add med page")
                }
                Button(
                    onClick = navigateToMedicationsList,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text("On medications list page")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.todayIntakes.isEmpty()) {
                AEmptySchedulePlaceholder()
            } else {
                val myLa: (Boolean, UUID, Long) -> Unit = { v1, v2, v3 -> }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(
                        items = state.todayIntakes,
                        key = { intake ->
                            intake.plannedIntakeId
                        }
                    ) { intake ->
                        AIntakeCard(
                            intake = intake,
                            // id получаем раньше, ниже передаем только статус и время.
                            onCheck = { status, time ->
                                viewModel.checkIntake(status, intake.plannedIntakeId, time)
                            }
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
private fun AEmptySchedulePlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("На сегодня приемов нет", style = MaterialTheme.typography.bodyLarge)
    }
}