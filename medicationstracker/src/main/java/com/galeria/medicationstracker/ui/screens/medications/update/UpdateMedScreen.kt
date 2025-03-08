package com.galeria.medicationstracker.ui.screens.medications.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.ui.componentsOld.DayOfWeekSelector
import com.galeria.medicationstracker.ui.componentsOld.FlyButton
import com.galeria.medicationstracker.ui.componentsOld.FlyErrorButton
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.componentsOld.FlyTonalButton
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.screens.medications.MedsPagesViewModel
import com.galeria.medicationstracker.ui.screens.medications.newmed.DateRangePickerModal
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.convertMillisToDate
import com.galeria.medicationstracker.utils.formatDateStringToTimestampMMMMddyyyy
import com.galeria.medicationstracker.utils.formatTimestampTillTheDayMMMMddyyyy
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateMedScreen(
    passedMedName: String,
    modifier: Modifier = Modifier,
    viewModel: UpdateMedVM = viewModel(),
    medsPagesVM: MedsPagesViewModel = viewModel(),
    onConfirmEdit: () -> Unit = {},
) {
    LaunchedEffect(Unit) { viewModel.fetchSelectedMedication(passedMedName) }
    
    val state = viewModel.uiState
    val currentMed by viewModel.selectedMedication.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(modifier = Modifier.padding(8.dp))

        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Name input.
            item {
                Text(
                    text = "Name",
                    style = MedTrackerTheme.typography.title2,
                )
                Spacer(modifier = Modifier.padding(4.dp))

                MyTextField(
                    value = state.medName,
                    onValueChange = { viewModel.updateMedName(it) },
                    label = "Medication name",
                    placeholder = currentMed?.name,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            // form.
            item {
                var selectedForm by remember { mutableStateOf(state.medForm) }
                val options = MedicationForm.entries.toTypedArray()

                FlySimpleCard {
                    Text(
                        text = "Form",
                        style = MedTrackerTheme.typography.title2,
                    )
                    Spacer(modifier = Modifier.padding(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        options.forEach { form ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = form.toString().lowercase())
                                RadioButton(
                                    selected = selectedForm == form,
                                    onClick = { selectedForm = form })
                            }
                        }
                    }
                }
            }
            // Start and End Date input
            item {
                FlySimpleCard {
                    DatePicker(viewModel)
                }
            }

            item {
                FlySimpleCard {
                    Text(
                        text = "Schedule",
                        style = MedTrackerTheme.typography.title2,
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    DayOfWeekSelector(
                        viewModelUpd = viewModel
                    )
                }
            }
            // Intake Time input
            item {
                var showTimePicker by remember { mutableStateOf(false) }

                FlyButton(
                    onClick = { showTimePicker = true }) {
                    Text("Set time")
                }

                if (showTimePicker) {
                    NewIntakeTimePicker(
                        onConfirm = {
                            showTimePicker = false
                        },
                        onDismiss = { showTimePicker = false },
                        viewModel
                    )
                }
            }
            // Notes input
            item {
                Text(
                    text = "Notes",
                    style = MedTrackerTheme.typography.title2,
                )
                Spacer(modifier = Modifier.padding(4.dp))

                MyTextField(
                    value = state.notes, // Assuming you have a medNotes state property
                    onValueChange = { viewModel.updateNotes(it) }, // Update the notes state property
                    label = "Medication notes",
                    placeholder = currentMed?.notes,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth(),
                    // maxLines = 4, // Adjust max lines as needed
                )
            }
            // Strength input
            item {
                Text(
                    text = "Strength",
                    style = MedTrackerTheme.typography.title2,
                )
                Spacer(modifier = Modifier.padding(4.dp))

                MyTextField(
                    value = state.strength.toString(), // Assuming you have a medStrength state property
                    onValueChange = { viewModel.updateStrength(it.toFloat()) }, // Update the strength state property
                    label = "Medication strength",
                    placeholder = currentMed?.strength.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                )
                // Add a unit selector or dropdown for strength units (e.g., MG, ML)
                // ...
            }

            item {
                val context = LocalContext.current

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    FlyButton(
                        // TODO: Add editing medication logic.
                        onClick = {
                            viewModel.updateMedicationFromFirestore(context)
                            onConfirmEdit.invoke()
                        }
                    ) {
                        Text("Confirm")
                    }

                    FlyErrorButton(
                        // TODO: Add editing medication logic.
                        onClick = onConfirmEdit
                    ) {
                        Text("Delete Medication")
                    }
                }
            }
        }
    }

}

@Composable
fun DatePicker(
    viewModel: UpdateMedVM
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        if (showPicker) {
            DateRangePickerModal(
                onDateRangeSelected = {
                    viewModel.updateStartDate(
                        formatDateStringToTimestampMMMMddyyyy(
                        convertMillisToDate(it.first)
                    ))
                    viewModel.updateEndDate(
                        formatDateStringToTimestampMMMMddyyyy(
                        convertMillisToDate(it.second)
                    ))
                    showPicker = !showPicker
                },
                onDismiss = { showPicker = !showPicker },
            )
        }
        MyTextField(
            value = "",
            label = "Start: ${formatTimestampTillTheDayMMMMddyyyy(viewModel.uiState.startDate)}\nEnd: ${
                formatTimestampTillTheDayMMMMddyyyy(
                    viewModel.uiState.endDate
                )
            }",
            onValueChange = {},
            isPrimaryColor = false,
            readOnly = true,
        )
        FlyButton(
            onClick = { showPicker = !showPicker },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Choose start and end dates")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIntakeTimePicker(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: UpdateMedVM
) {
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )
    val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
    val dtf = DateTimeFormatter.ofPattern("HH:mm")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimePicker(
            state = timePickerState,
        )
        FlyButton(onClick = {
            viewModel.updateIntakeTime(time.format(dtf))
            onDismiss.invoke();
        }) {
            Text("Confirm Time")
        }
        FlyTonalButton(onClick = onDismiss) {
            Text("Dismiss")
        }
    }
}