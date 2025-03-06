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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.ui.componentsOld.FLySimpleCardContainer
import com.galeria.medicationstracker.ui.componentsOld.LogMedicationTimeDialog
import com.galeria.medicationstracker.ui.componentsOld.WeeklyCalendarView
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography
import com.google.firebase.Timestamp

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onAddMedClick: () -> Unit,
    dashboardViewModel: DashboardVM,
) {
    val uiState = dashboardViewModel.uiState.collectAsStateWithLifecycle()
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // today's date.
        Text(
            text = uiState.value.todayDate,
            style = typography.display3Emphasized
        )
        // Календарь на неделю.
        WeeklyCalendarView()
        // Medication Cards List.
        val medicationsByIntakeTime =
            dashboardViewModel.groupMedicationsByIntakeTime(uiState.value.currentTakenMedications)
        MedsByIntakeTimeList(
            onAddNoteClick = {
                onAddMedClick.invoke()
            },
            medicationsForIntakeTime = uiState.value.currentTakenMedications,
            medicationsByIntakeTime = medicationsByIntakeTime
        )
    }
}

// Список лекарств по времени приема.
@Composable
fun MedsByIntakeTimeList(
    onLogIntakeClick: () -> Unit = {},
    onAddNoteClick: () -> Unit,
    medicationsForIntakeTime: List<UserMedication> = emptyList(),
    medicationsByIntakeTime: Map<String, List<UserMedication>> = emptyMap(),
) {

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
                                fetchIntakeStatus = { medication ->
                                    dashboardViewModel.fetchIntakeStatus(
                                        medication
                                    )
                                },
                                addNewIntake = { time, medication, status ->
                                    dashboardViewModel.addNewIntake(
                                        time,
                                        medication,
                                        status
                                    )
                                }
                                medication = medicationsForIntakeTime,
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
    fetchIntakeStatus: (UserMedication) -> Int,
    addNewIntake: (Timestamp, UserMedication, Boolean) -> Unit,
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
            status = fetchIntakeStatus(medication)
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
                onDismiss = {
                },
                onAddNotes = {
                    onAddNoteClick.invoke()
                    showLogDialog.value = false
                },
                onConfirmTime = { time ->
                    addNewIntake(
                        time,
                        medication,
                        false
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
