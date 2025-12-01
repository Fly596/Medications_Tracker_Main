package com.galeria.medicationstracker.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
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
import com.galeria.medicationstracker.data.source.network.IntakeStatus
import com.galeria.medicationstracker.data.source.network.NetworkIntake
import com.galeria.medicationstracker.data.source.network.NetworkMedication
import com.galeria.medicationstracker.ui.components.GFABButton
import com.galeria.medicationstracker.ui.componentsOld.FLySimpleCardContainer
import com.galeria.medicationstracker.ui.componentsOld.LogMedicationTimeDialog
import com.galeria.medicationstracker.ui.componentsOld.WeeklyCalendarView
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography
import com.galeria.medicationstracker.utils.getTodaysDate
import com.google.firebase.Timestamp
import java.time.format.DateTimeFormatter

// Главная страница после входа.
@Composable
fun DailyMedsScreen(
    modifier: Modifier = Modifier,
    onAddMood: () -> Unit = {},
    onAddIntake: () -> Unit,
    dashboardViewModel: DailyMedsVM = hiltViewModel(),
) {
    val uiState = dashboardViewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MedTrackerTheme.colors.secondaryBackground,
        topBar = {
            Column(
                modifier = Modifier.padding(
                    vertical = 24.dp,
                    horizontal = 16.dp
                )
            ) {
                // today's date.
                Text(
                    text = getTodaysDate().format(
                        DateTimeFormatter.ofPattern(
                            "MMM d"
                        )
                    ),
                    style = typography.display3Emphasized,
                )
            }
        },
        floatingActionButton = {
            GFABButton(
                onClick = onAddMood
                // mood tracker
            )
        },
    ) { innerPadding ->
        if(uiState.value.isLoading){
            CircularProgressIndicator()
        }else{
            Column(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Календарь на неделю.
                WeeklyCalendarView()
                // Medication Cards List.
                 MedsByIntakeTimeList(
                    viewModel = dashboardViewModel,
                    onAddNoteClick = { onAddIntake.invoke() },
                    medicationsForIntakeTime = uiState.value.currentTakenMedications,
                     formatTime = dashboardViewModel::formatTime
                )
           /*      NewMedsByIntakeTimeList(
                    onAddNoteClick = { onAddIntake.invoke() },
                    intakeList = uiState.value.todayIntakes,
                    onAddIntakeAction = { time, intake, status ->
                        dashboardViewModel.newAddNewIntake(time, intake, status)
                    }
                    // newAddNewIntake()
                ) */
            }
        }
    
    }
}
// ___
// Список приемов по времени приема.
@Composable
fun NewMedsByIntakeTimeList(
    onAddNoteClick: () -> Unit = {},
    intakeList: List<NetworkIntake> = emptyList(),
    onAddIntakeAction: (Timestamp, NetworkIntake, IntakeStatus) -> Unit,
    formatTime: (Int) -> String = { "" }
) {
    // Группируем лекарства по времени приема.
    val medicationsByIntakeTime =
        intakeList.groupBy { it.presetTimeFromMidnight }
    
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        medicationsByIntakeTime.forEach { (presetTime, medications) ->
            item {
                // Контейнер для каждого времени приема.
                FLySimpleCardContainer(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        // Время приема.
                        Text(
                            text = formatTime(presetTime).toString(), // TODO: Перевести в строку
                            style = typography.title1Emphasized,
                            modifier = Modifier.padding(0.dp),
                        )
                        // Лекарства на это время.
                        medications.forEach { medicationsForIntakeTime ->
                            NewMedicationIntakeItem(
                                intake = medicationsForIntakeTime,
                                onAddNoteClick = { onAddNoteClick.invoke() },
                                onAddIntake = onAddIntakeAction
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NewMedicationIntakeItem(
    intake: NetworkIntake,
    icon: ImageVector = Icons.Filled.Medication,
    onAddNoteClick: () -> Unit = {},
    onAddIntake: (Timestamp, NetworkIntake, IntakeStatus) -> Unit
    // onAddIntake: (intakeTime: Timestamp,medication: NetworkMedication, status: IntakeStatus ) ->Unit
) {
    val showLogDialog = rememberSaveable { mutableStateOf(false) }
    
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
        
        Text(text = intake.name, style = typography.bodyLarge)
        
        Spacer(modifier = Modifier.weight(1f))
        // State to control the check icon.
        var status by remember { mutableIntStateOf(0) }
        
        
        Text(
            text =
                when (intake.status) {
                    "TAKEN" -> "Taken"
                    "SKIPPED" -> "Skipped"
                    "PENDING" -> "Pending"
                    else -> ""
                },
            style = typography.bodySmall,
            color = MedTrackerTheme.colors.secondaryLabel,
        )
        
        IconButton(
            onClick = {
                // Add logic to log medication here.
                showLogDialog.value = !showLogDialog.value
            }
        ) {
            Icon(
                imageVector =
                    when (intake.status) {
                        "TAKEN" -> Icons.Filled.CheckCircle
                        "SKIPPED" -> Icons.Filled.CheckCircle
                        else -> Icons.Outlined.CheckCircle
                    },
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint =
                    when (intake.status) {
                        "TAKEN" -> MedTrackerTheme.colors.sysSuccess
                        "SKIPPED" -> MedTrackerTheme.colors.sysWarning
                        else -> MedTrackerTheme.colors.tertiaryLabel
                    },
            )
        }
        // ФУНКЦИЯ newAddNewIntake БУДЕТ ИСПОЛЬЗОВАТЬСЯ ЗДЕСЬ.
        if (showLogDialog.value) {
            LogMedicationTimeDialog(
                onDismiss = { showLogDialog.value = false },
                onConfirmation = {
                    onAddIntake(Timestamp.now(), intake, IntakeStatus.TAKEN)
                    showLogDialog.value = false
                },
                onAddNotes = {
                    onAddNoteClick.invoke()
                    showLogDialog.value = false
                },
                onConfirmTime = { time ->
                    onAddIntake(time, intake, IntakeStatus.TAKEN)
                },
                onSkipIntake = {
                    onAddIntake(Timestamp.now(), intake, IntakeStatus.SKIPPED)
                    showLogDialog.value = false
                },
            )
        }
    }
}
// ___
// region old
// Список лекарств по времени приема.
@Composable
fun MedsByIntakeTimeList(
    viewModel: DailyMedsVM,
    onAddNoteClick: () -> Unit = {},
    medicationsForIntakeTime: List<NetworkMedication> = emptyList(),
    formatTime: (Int) -> String = { "" }
) {
    // Группируем лекарства по времени приема.
    val medicationsByIntakeTime =
        medicationsForIntakeTime.groupBy { it.intakeTimeFromMidnight }
    
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        medicationsByIntakeTime.forEach { (intakeTime, medications) ->
            item {
                // Контейнер для каждого времени приема.
                FLySimpleCardContainer(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        // Время приема.
                        Text(
                            text = formatTime(intakeTime),
                            style = typography.title1Emphasized,
                            modifier = Modifier.padding(0.dp),
                        )
                        // Лекарства на это время.
                        medications.forEach { medicationsForIntakeTime ->
                            MedicationItem(
                                viewModel = viewModel,
                                medication = medicationsForIntakeTime,
                                onAddNoteClick = { onAddNoteClick.invoke() },
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
    viewModel: DailyMedsVM,
    medication: NetworkMedication,
    icon: ImageVector = Icons.Filled.Medication,
    onAddNoteClick: () -> Unit = {},
   // onAddIntake: (intakeTime: Timestamp,medication: NetworkMedication, status: IntakeStatus ) ->Unit
) {
    val showLogDialog = rememberSaveable { mutableStateOf(false) }
    
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
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
            text =
                when (status) {
                    2 -> "Taken"
                    1 -> "Skipped"
                    else -> ""
                },
            style = typography.bodySmall,
            color = MedTrackerTheme.colors.secondaryLabel,
        )
        
        IconButton(
            onClick = {
                // Add logic to log medication here.
                showLogDialog.value = !showLogDialog.value
            }
        ) {
            Icon(
                imageVector =
                    when (status) {
                        2 -> Icons.Filled.CheckCircle
                        1 -> Icons.Filled.CheckCircle
                        else -> Icons.Outlined.CheckCircle
                    },
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint =
                    when (status) {
                        2 -> MedTrackerTheme.colors.sysSuccess
                        1 -> MedTrackerTheme.colors.sysWarning
                        else -> MedTrackerTheme.colors.tertiaryLabel
                    },
            )
        }
        // Display the dialog when `showLogDialog.value` is true
        if (showLogDialog.value) {
            LogMedicationTimeDialog(
                onDismiss = { showLogDialog.value = false },
                onConfirmation = {
                    // TODO: Remove
                    viewModel.addNewIntake(
                        medication = medication,
                        status = IntakeStatus.TAKEN
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
                        medication = medication,
                        status = IntakeStatus.TAKEN,
                    )
                },
                onSkipIntake = {
                    viewModel.addNewIntake(
                        medication = medication,
                        status = IntakeStatus.SKIPPED
                    )
                    showLogDialog.value = false
                },
            )
        }
    }
}
// endregion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyMedsScreenNew(
    modifier: Modifier = Modifier,
    onAddMood: () -> Unit = {},
    onAddIntake: (String) -> Unit = {},
    dashboardViewModel: DailyMedsVM = hiltViewModel(),
) {
    val state = dashboardViewModel.uiState.collectAsStateWithLifecycle()
    val inputState =
        dashboardViewModel.intakeInputState.collectAsStateWithLifecycle()
    
    MedTrackerTheme {
        Scaffold(
            containerColor = MedTrackerTheme.colors.secondaryBackground,
            topBar = {
                Column(
                    modifier = Modifier.padding(
                        vertical = 24.dp,
                        horizontal = 16.dp
                    )
                ) {
                    // today's date.
                    Text(
                        text = getTodaysDate().format(
                            DateTimeFormatter.ofPattern(
                                "MMM d"
                            )
                        ),
                        style = typography.display3Emphasized,
                    )
                }
            },
            floatingActionButton = {
                // Add mood value.
                GFABButton(onClick = { onAddMood.invoke() })
            },
        ) { innerPadding ->
            Column(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Календарь на неделю.
                WeeklyCalendarView()
                // Medication Cards List.
            }
        }
    }
}

@Composable
fun TodayIntakesList(todayMedicationsList: List<NetworkMedication>) {
    // группировка по времени приема.
    val medicationsGroupedByTime =
        todayMedicationsList.groupBy { it.intakeTimeFromMidnight }
    
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        medicationsGroupedByTime.forEach { (intakeTime, medications) ->
            item {
                FLySimpleCardContainer {
                    Column {
                        // Время приема.
                        Text(
                            text = intakeTime.toString(),
                            style = typography.title1Emphasized,
                            modifier = Modifier.padding(0.dp),
                        )
                        medications.forEach {
                            MedicationItemNew(
                                it,
                                // TODO
                                onLogTaken = {},
                                onLogSkipped = {},
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationItemNew(
    medication: NetworkMedication,
    onLogTaken: () -> Unit,
    onLogSkipped: () -> Unit,
) {
}

@Preview(name = "StartScreen")
@Composable
private fun PreviewStartScreen() {
    // StartScreen("empty")
}
