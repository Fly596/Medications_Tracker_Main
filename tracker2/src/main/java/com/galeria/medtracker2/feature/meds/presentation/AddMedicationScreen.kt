package com.galeria.medtracker2.feature.meds.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.components.DateSelectionRow
import com.galeria.medtracker2.core.ui.components.TimeSelectionRow

@Composable
fun AddMedicationScreen(viewModel: AddMedicationVM = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Поля ввода основной информации.
        TextField(
            value = state.name,
            onValueChange = viewModel::updateName,
            label = { Text("Medication name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
        TextField(
            value = state.dose,
            onValueChange = viewModel::updateDose,
            label = { Text("Medication dose") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
        )

        // Блок выбора дат и времени.
        DateSelectionRow(
            label = "Start Date",
            selectedDateString = state.startDateString,
            onDateSelected = viewModel::updateStartDate
        )
        DateSelectionRow(
            label = "End Date",
            selectedDateString = state.endDateString,
            onDateSelected = viewModel::updateEndDate
        )
        TimeSelectionRow(
            label = "Time",
            selectedTimeString = state.intakeTimeString,
            onTimeSelected = viewModel::updateTime
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = viewModel::addMedication,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Set alarm")
        }
    }
}
