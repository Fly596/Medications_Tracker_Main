package com.galeria.medicationstracker.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.ui.componentsOld.FLySimpleCardContainer
import com.galeria.medicationstracker.ui.componentsOld.LogMedicationTimeDialog
import com.galeria.medicationstracker.ui.componentsOld.WeeklyCalendarView
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography
import com.galeria.medicationstracker.utils.getTodaysDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onViewLogsClick: () -> Unit = {},
    onAddMedClick: () -> Unit,
    dashboardViewModel: DashboardVM = hiltViewModel(),
) {
    val uiState = dashboardViewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        containerColor = MedTrackerTheme.colors.secondaryBackground,
        topBar = {
            // today's date.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = getTodaysDate().format(DateTimeFormatter.ofPattern("MMM d")),
                    style = typography.display3Emphasized
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Календарь на неделю.
            WeeklyCalendarView()
            // Medication Cards List.
            MedsByIntakeTimeList(
                viewModel = dashboardViewModel,
                onAddNoteClick = {
                    onAddMedClick.invoke()
                },
                medicationsForIntakeTime = uiState.value.currentTakenMedications
            )
        }
    }
    

}

// Список лекарств по времени приема.
@Composable
fun MedsByIntakeTimeList(
    viewModel: DashboardVM,
    onAddNoteClick: () -> Unit = {},
    medicationsForIntakeTime: List<UserMedication> = emptyList()
) {
    // Группируем лекарства по времени приема.
    val medicationsByIntakeTime =
        medicationsForIntakeTime.groupBy { it.intakeTime }
    
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        medicationsByIntakeTime.forEach { (intakeTime, medications) ->
            item {
                // Контейнер для каждого времени приема.
                FLySimpleCardContainer(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Время приема.
                        Text(
                            text = intakeTime.toString(),
                            style = typography.title1Emphasized,
                            modifier = Modifier.padding(0.dp)
                        )
                        // Лекарства на это время.
                        medications.forEach { medicationsForIntakeTime ->
                            MedicationItem(
                                viewModel = viewModel,
                                medication = medicationsForIntakeTime,
                                onAddNoteClick = { onAddNoteClick.invoke() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationItem(
    viewModel: DashboardVM,
    medication: UserMedication,
    icon: ImageVector = Icons.Filled.Medication,
    onAddNoteClick: () -> Unit = {},
) {
    val showLogDialog = rememberSaveable { mutableStateOf(false) }
    
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        
        Text(text = medication.name.toString(), style = typography.bodyLarge)
        
        Spacer(modifier = Modifier.weight(1f))
        // State to control the check icon.
        var status by remember { mutableIntStateOf(0) }
        LaunchedEffect(medication) {
            status = viewModel.fetchIntakeStatus(medication)
        }
        
        Text(
            text = when (status) {
                2 -> "Taken"
                1 -> "Skipped"
                else -> ""
            },
            style = typography.bodySmall,
            color = MedTrackerTheme.colors.secondaryLabel
        )
        
        IconButton(
            onClick = {
                // Add logic to log medication here.
                showLogDialog.value = !showLogDialog.value
            }) {
            Icon(
                imageVector = when (status) {
                    2 -> Icons.Filled.CheckCircle
                    1 -> Icons.Filled.CheckCircle
                    else -> Icons.Outlined.CheckCircle
                },
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = when (status) {
                    2 -> MedTrackerTheme.colors.sysSuccess
                    1 -> MedTrackerTheme.colors.sysWarning
                    else -> MedTrackerTheme.colors.tertiaryLabel
                }
            )
        }
        // Display the dialog when `showLogDialog.value` is true
        if (showLogDialog.value) {
            LogMedicationTimeDialog(
                medication = medication,
                onDialogDismissed = {
                    showLogDialog.value = false
                },
                onTimeConfirmed = {
                    viewModel.addNewIntake(
                        medication = medication,
                        status = true
                    )
                    showLogDialog.value = false
                },
                onAddNotes = {
                    onAddNoteClick.invoke()
                    showLogDialog.value = false
                },
                onConfirmTime = { time ->
                    viewModel.addNewIntake(
                        intakeTime = time,
                        medication = medication
                    )
                }
            )
        }
    }
}

@Preview(name = "StartScreen")
@Composable
private fun PreviewStartScreen() {
    // StartScreen("empty")
}
