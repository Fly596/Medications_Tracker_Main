package com.galeria.medicationstracker.ui.screens.medications.update

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.ui.componentsOld.DayOfWeekSelector
import com.galeria.medicationstracker.ui.componentsOld.FlyButton
import com.galeria.medicationstracker.ui.componentsOld.FlyErrorButton
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.componentsOld.FlyTonalButton
import com.galeria.medicationstracker.ui.componentsOld.MyTextField
import com.galeria.medicationstracker.ui.screens.medications.newmed.DateRangePickerModalOld
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography
import com.galeria.medicationstracker.utils.convertMillisToDate
import com.galeria.medicationstracker.utils.formatDateStringToTimestampMMMMddyyyy
import com.google.firebase.Timestamp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun UpdateMedScreen(
    // passedMedId: String,
    modifier: Modifier = Modifier,
    viewModel: UpdateMedVM = hiltViewModel(),
    onBack: () -> Unit = {},
) {
    // LaunchedEffect(passedMedId) { viewModel.fetchSelectedMedication(passedMedId) }
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val currentMed = state.value.medication

    MedTrackerTheme {
        Scaffold(
            containerColor = MedTrackerTheme.colors.secondaryBackground,
            topBar = {
                Row(modifier = Modifier.padding(vertical = 24.dp)) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MedTrackerTheme.colors.primaryLabel,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                    Text(
                        text = (stringResource(R.string.update_medication_title)),
                        style = typography.display3Emphasized,
                    )
                }
            },
        ) { innerPadding ->
            /*  Column(
                modifier =
                    modifier.fillMaxWidth().padding(innerPadding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) { */
            LazyColumn(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // Name input.
                item {
                    Text(text = "Name", style = typography.title2)
                    Spacer(modifier = Modifier.padding(4.dp))

                    MyTextField(
                        value = state.value.medName,
                        onValueChange = { viewModel.updateMedName(it) },
                        label = stringResource(R.string.medication_name),
                        placeholder = currentMed?.name,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                // form.
                item {
                    var selectedForm by remember { mutableStateOf(state.value.medForm) }
                    // var selectedForm = state.value.medForm
                    val options = MedicationForm.entries.toTypedArray()

                    FlySimpleCard {
                        Text(
                            text = stringResource(R.string.form),
                            style = typography.title2,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            options.forEach { form ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = form.toString().lowercase())
                                    /*          RadioButton(
                                                 selected = selectedForm == form,
                                                 onClick = { viewModel.updateMedForm(form) },
                                             ) */
                                }
                            }
                        }
                    }
                }
                // Start and End Date input
                val start = state.value.startDate
                val end = state.value.endDate
                item {
                    FlySimpleCard {
                        DatePicker(
                            { viewModel.updateStartDate(it) },
                            { viewModel.updateEndDate(it) },
                        )
                    }
                }

                item {
                    FlySimpleCard {
                        Text(
                            text = stringResource(R.string.schedule),
                            style = typography.title2,
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        DayOfWeekSelector(viewModelUpd = viewModel)
                    }
                }
                // Intake Time input
                item {
                    var showTimePicker by remember { mutableStateOf(false) }

                    FlyButton(onClick = { showTimePicker = true }) {
                        Text(stringResource(R.string.set_time))
                    }

                    if (showTimePicker) {
                        NewIntakeTimePicker(
                            onConfirm = { showTimePicker = false },
                            onDismiss = { showTimePicker = false },
                            viewModel,
                        )
                    }
                }
                // Notes input
                item {
                    Text(
                        text = stringResource(R.string.notes),
                        style = typography.title2,
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    // MyTextField(
                    //     value = state.value.notes, // Assuming you have a medNotes state property
                    //     onValueChange = {
                    //         viewModel.updateNotes(it)
                    //     }, // Update the notes state property
                    //     label = stringResource(R.string.medication_notes),
                    //     placeholder = currentMed?.name,
                    //     keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    //     modifier = Modifier.fillMaxWidth(),
                    //     // maxLines = 4, // Adjust max lines as needed
                    // )
                }
                // Strength input
                item {
                    Text(
                        text = stringResource(R.string.medication_strength),
                        style = typography.title2,
                    )
                    Spacer(modifier = Modifier.padding(4.dp))

                    MyTextField(
                        value =
                            state.value.dosage
                                .toString(), // Assuming you have a medStrength state property
                        onValueChange = {
                            if (it.isEmpty()) {
                                viewModel.updateStrength(0f)
                            } else {
                                it.toFloatOrNull()?.let { strength ->
                                    viewModel.updateStrength(strength)
                                }
                            }
                        }, // Update the strength state property
                        label = stringResource(R.string.medication_strength),
                        placeholder = currentMed?.dosage.toString(),
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
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        FlyButton(
                            onClick = {
                                viewModel.updateMedicationFromFirestore(context)
                                onBack.invoke()
                            }
                        ) {
                            Text("Confirm")
                        }

                        FlyErrorButton(
                            onClick = {
                                viewModel.deleteMedicationFromFirestore(
                                    currentMed!!.id
                                )
                            }
                        ) {
                            Text(stringResource(R.string.delete_medication))
                        }
                    }
                }
            }
            // }
        }
    }
}

@Composable
fun DatePicker(
    // viewModel: UpdateMedVM,
    updateStartDate: (Timestamp) -> Unit,
    updateEndDate: (Timestamp) -> Unit,
) {
    var showPicker by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.Center) {
        if (showPicker) {
            DateRangePickerModalOld(
                onDateRangeSelected = {
                    formatDateStringToTimestampMMMMddyyyy(convertMillisToDate(it.first))?.let { it1
                        ->
                        updateStartDate(it1)
                    }
                    formatDateStringToTimestampMMMMddyyyy(convertMillisToDate(it.second))?.let { it1
                        ->
                        updateEndDate(it1)
                    }
                    /*      viewModel.updateStartDate(
                        formatDateStringToTimestampMMMMddyyyy(convertMillisToDate(it.first))
                    )
                    viewModel.updateEndDate(
                        formatDateStringToTimestampMMMMddyyyy(convertMillisToDate(it.second))
                    ) */
                    showPicker = !showPicker
                },
                onDismiss = { showPicker = !showPicker },
            )
        }
        /*         Text(
            text =
                "Start: ${formatTimestampTillTheDayMMMMddyyyy(viewModel.uiState.value.startDate)}"
        )
        MyTextField(
            value = "",
            label =
                "Start: ${formatTimestampTillTheDayMMMMddyyyy(viewModel.uiState.value.startDate)}\nEnd: ${
                formatTimestampTillTheDayMMMMddyyyy(
                    viewModel.uiState.value.endDate
                )
            }",
            onValueChange = {},
            isPrimaryColor = false,
            readOnly = true,
        ) */
        FlyButton(onClick = { showPicker = !showPicker }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(R.string.choose_start_and_end_dates))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewIntakeTimePicker(onConfirm: () -> Unit, onDismiss: () -> Unit, viewModel: UpdateMedVM) {
    val currentTime = Calendar.getInstance()
    val timePickerState =
        rememberTimePickerState(
            initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
            initialMinute = currentTime.get(Calendar.MINUTE),
            is24Hour = false,
        )
    val time = LocalTime.of(timePickerState.hour, timePickerState.minute)
    val dtf = DateTimeFormatter.ofPattern("HH:mm")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        TimePicker(state = timePickerState)
        FlyButton(
            onClick = {
                viewModel.updateIntakeTime(time.format(dtf))
                onDismiss.invoke()
            }
        ) {
            Text("Confirm Time")
        }
        FlyTonalButton(onClick = onDismiss) { Text("Dismiss") }
    }
}
