package com.galeria.medtracker2.feature.intakes.presentation.add_intake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.R
import com.galeria.medtracker2.core.ui.components.DatePickerModalInput
import com.galeria.medtracker2.core.ui.components.TimePickerDialogNew
import java.time.LocalTime

@Composable
fun AddIntakeScreen(
    onNavigateBack: () -> Unit = {},
    onAddIntake: () -> Unit = {},
    viewModel: AddIntakeVM = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddIntakeContent(
        uiState = uiState,
        onDosageChange = viewModel::updateDosage,
        onTimeChange = viewModel::updateTime,
        onDateChange = viewModel::updateDate,
        onAddIntake = onAddIntake,
        onDatePicker = viewModel::updateDatePicker,
        onTimePicker = viewModel::updateTimePicker,
        modifier = Modifier
    )
}

@Composable
fun AddIntakeContent(
    uiState: AddIntakeUiState,
    onDosageChange: (String) -> Unit,
    onTimeChange: (LocalTime) -> Unit,
    onDateChange: (Long?) -> Unit,
    onAddIntake: () -> Unit,
    onDatePicker: () -> Unit,
    onTimePicker: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "New dose",
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = uiState.dosage,
                onValueChange = onDosageChange,
                label = { Text("Dosage") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Row() {
                Button(
                    onClick = { onDatePicker.invoke() }
                ) {
                    Text(uiState.date)
                }
                Button(
                    onClick = onTimePicker
                ) {
                    Text("Pick time")
                }
            }


            if (uiState.isShowingDatePicker) {
                DatePickerModalInput(onDateSelected = {
                    onDateChange.invoke(it)
                }, onDismiss = {
                    onDatePicker.invoke()
                })
            }
            if (uiState.isShowingTimePicker) {
                TimePickerDialogNew(onConfirm = onTimeChange, onDismiss = {
                    onTimePicker
                })
            }
        }
    }
}

@Composable
fun DatePickerCard(
    datePickerState: DatePickerState,
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    Card(
        onClick = {
            showDatePicker = true
        },
    ) {
        Row() {
            Icon(
                painterResource(R.drawable.edit_calendar_24px),
                contentDescription = null
            )
            Text(
                text = datePickerState.selectedDateMillis.toString(),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}