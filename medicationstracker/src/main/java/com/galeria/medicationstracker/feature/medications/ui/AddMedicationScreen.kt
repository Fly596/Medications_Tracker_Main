package com.galeria.medicationstracker.feature.medications.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDateRangePickerState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.core.ui.components.DayOfWeekSelector
import com.galeria.medicationstracker.core.ui.components.FlySimpleCard
import com.galeria.medicationstracker.core.ui.components.GDropdownList
import com.galeria.medicationstracker.core.ui.components.GPrimaryButton
import com.galeria.medicationstracker.core.ui.components.GRadioButton
import com.galeria.medicationstracker.core.ui.components.GSecondaryButton
import com.galeria.medicationstracker.core.ui.components.GTextField
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme.typography
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.old.MedicationUnit
import com.galeria.medicationstracker.feature.medications.presentation.AddMedicationViewModel
import com.galeria.medicationstracker.utils.DateTimeUtils
import com.galeria.medicationstracker.utils.convertMillisToDate
import com.galeria.medicationstracker.utils.formatDateStringToTimestampMMMMddyyyy
import com.galeria.medicationstracker.utils.toLocalDate
import java.time.LocalTime
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: AddMedicationViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var showTimePicker by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Navigate back when medication saving completes successfully
    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            navigateBack()
        }
    }

    // Display error message via Snackbar if validation or persistence fails
    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { errorMsg ->
            snackbarHostState.showSnackbar(errorMsg)
            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = MedTrackerTheme.colors.secondaryBackground,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_medication),
                        style = typography.display3Emphasized
                    )
                },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MedTrackerTheme.colors.primaryLabel,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MedTrackerTheme.colors.secondaryBackground
                ),
                windowInsets = WindowInsets(top = 0, bottom = 0),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Name & Form selection
                item {
                    FlySimpleCard(
                        isPrimaryBackground = true,
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                GTextField(
                                    value = state.medName,
                                    onValueChange = { viewModel.updateMedName(it) },
                                    label = "Medication Name",
                                    isPrimaryColor = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier.fillMaxWidth(),
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Medication Form",
                                        style = typography.title3Emphasized
                                    )
                                    val medFormOptionsList = MedicationForm.entries.map { it.name }
                                    GDropdownList(items = medFormOptionsList) { selected ->
                                        viewModel.updateMedForm(selected)
                                    }
                                }
                            }
                        }
                    )
                }

                // Dosage Strength & Units
                item {
                    val unitOptions = MedicationUnit.entries.toTypedArray()

                    FlySimpleCard(
                        isPrimaryBackground = true,
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                GTextField(
                                    value = state.medStrength,
                                    onValueChange = { viewModel.updateMedStrength(it) },
                                    label = stringResource(R.string.medication_strength),
                                    isPrimaryColor = true,
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier.fillMaxWidth(),
                                )

                                Text(
                                    text = "Unit",
                                    style = typography.title3Emphasized
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                ) {
                                    unitOptions.forEach { unit ->
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = unit.name.lowercase(),
                                                style = typography.bodyMedium
                                            )
                                            GRadioButton(
                                                selected = state.medUnit == unit,
                                                onClick = {
                                                    viewModel.updateMedUnit(unit)
                                                },
                                            )
                                        }
                                    }
                                }
                            }
                        },
                    )
                }

                // Start & End Dates + Intake Time
                item {
                    FlySimpleCard(
                        isPrimaryBackground = true,
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.intake_period),
                                    style = typography.title2Emphasized,
                                )

                                // Date display summary row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.CalendarToday,
                                        contentDescription = null,
                                        tint = MedTrackerTheme.colors.primary500,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    val startDateStr = state.medStartDate?.toLocalDate()
                                        ?.format(DateTimeUtils.dateFormatter) ?: "Not set"
                                    val endDateStr = state.medEndDate?.toLocalDate()
                                        ?.format(DateTimeUtils.dateFormatter) ?: "Not set"
                                    Text(
                                        text = "$startDateStr — $endDateStr",
                                        style = typography.bodyMedium
                                    )
                                }

                                GSecondaryButton(
                                    shape = MedTrackerTheme.shapes.medium,
                                    onClick = { showDatePicker = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Select Date Range")
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // Intake time display row
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.AccessTime,
                                        contentDescription = null,
                                        tint = MedTrackerTheme.colors.primary500,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text(
                                        text = "Intake Time: ${state.medIntakeTime.ifBlank { "08:00" }}",
                                        style = typography.bodyMedium
                                    )
                                }

                                GSecondaryButton(
                                    shape = MedTrackerTheme.shapes.medium,
                                    onClick = { showTimePicker = true },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(stringResource(R.string.set_time))
                                }
                            }
                        }
                    )
                }

                // Days of Week Selection
                item {
                    FlySimpleCard(
                        isPrimaryBackground = true,
                        modifier = Modifier.fillMaxWidth(),
                        content = {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                            ) {
                                Text(
                                    text = stringResource(R.string.choose_days),
                                    style = typography.title2Emphasized,
                                )
                                DayOfWeekSelector(viewModel = viewModel)
                            }
                        },
                    )
                }

                // Save Button
                item {
                    GPrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isSaving,
                        onClick = {
                            viewModel.addMedication()
                        },
                        content = {
                            if (state.isSaving) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = MedTrackerTheme.colors.sysWhite,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = stringResource(R.string.add_medication),
                                    style = typography.labelLargeEmphasized.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }

            // Time Picker Dialog
            if (showTimePicker) {
                IntakeTimePicker(
                    onConfirm = { showTimePicker = false },
                    onDismiss = { showTimePicker = false },
                    onSelectTime = { viewModel.updateIntakeTime(it) },
                )
            }

            // Date Range Picker Dialog
            if (showDatePicker) {
                DateRangePickerModalOld(
                    onDateRangeSelected = { (startMillis, endMillis) ->
                        if (startMillis != null) {
                            viewModel.updateStartDate(
                                formatDateStringToTimestampMMMMddyyyy(
                                    convertMillisToDate(startMillis)
                                )
                            )
                        }
                        if (endMillis != null) {
                            viewModel.updateEndDate(
                                formatDateStringToTimestampMMMMddyyyy(
                                    convertMillisToDate(endMillis)
                                )
                            )
                        }
                    },
                    onDismiss = { showDatePicker = false },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModalOld(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit,
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis,
                        )
                    )
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = { Text(text = "Select date range") },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntakeTimePicker(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onSelectTime: (String) -> Unit,
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .width(IntrinsicSize.Max),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Select Intake Time",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                )
                val currentTime = Calendar.getInstance()
                val timePickerState = rememberTimePickerState(
                    initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
                    initialMinute = currentTime.get(Calendar.MINUTE),
                    is24Hour = false,
                )
                val time = LocalTime.of(timePickerState.hour, timePickerState.minute)

                TimePicker(
                    state = timePickerState,
                    colors = TimePickerDefaults.colors(
                        clockDialColor = MaterialTheme.colorScheme.surfaceVariant,
                        clockDialSelectedContentColor = MaterialTheme.colorScheme.onSurface,
                        clockDialUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectorColor = MaterialTheme.colorScheme.primary,
                        periodSelectorBorderColor = MaterialTheme.colorScheme.outline,
                        periodSelectorSelectedContentColor = MaterialTheme.colorScheme.onSurface,
                        periodSelectorUnselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        periodSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        timeSelectorSelectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        timeSelectorUnselectedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    TextButton(
                        onClick = onDismiss,
                        colors = androidx.compose.material3.ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                    ) {
                        Text("Cancel")
                    }

                    GPrimaryButton(
                        onClick = {
                            onSelectTime(time.format(DateTimeUtils.timeFormatter))
                            onConfirm()
                        }
                    ) {
                        Text(
                            stringResource(R.string.confirm),
                            color = MedTrackerTheme.colors.sysWhite,
                        )
                    }
                }
            }
        }
    }
}