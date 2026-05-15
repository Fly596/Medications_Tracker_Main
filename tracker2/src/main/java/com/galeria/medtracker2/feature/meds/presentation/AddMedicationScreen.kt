package com.galeria.medtracker2.feature.meds.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.components.DateSelectionRow
import com.galeria.medtracker2.core.ui.components.TimeSelectionButton
import com.galeria.medtracker2.core.ui.components.TimeSelectionRow
import com.galeria.medtracker2.core.ui.components.rememberNotificationPermissionHandler

@Composable
fun AddMedicationScreen(
    onMainClick: () -> Unit = {},
    viewModel: AddMedicationVM = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Получаем функцию-триггер.
    val requestPermission = rememberNotificationPermissionHandler { isGranted ->
        if (isGranted) {
            // Добавляем лекарство.
            viewModel.addMedication()
        } else {
            println("Permission denied. Notifications won't work.")
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("New medication", style = MaterialTheme.typography.displaySmall) }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // region Поля ввода основной информации.
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
            // endregion
            // region Блок выбора дат и времени.
            DateSelectionRow(
                label = "Start Date",
                selectedDateString = state.startDateString,
                onDateSelected = viewModel::updateStartDate,
                modifier = Modifier.fillMaxWidth(),
            )
            DateSelectionRow(
                label = "End Date",
                selectedDateString = state.endDateString,
                onDateSelected = viewModel::updateEndDate,
                modifier = Modifier.fillMaxWidth(),
            )
            TimeSelectionRow(
                label = "Time",
                selectedTimeString = state.intakeTimeString,
                onTimeSelected = viewModel::updateTime,
                modifier = Modifier.fillMaxWidth(),
            )
            // endregion
            // Грид выбранных значений времени приема.
            LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 90.dp)) {
                items(items = state.intakeTimes) { intTimes ->
                    TimeSelectionButton(
                        label = "%02d:%02d".format(intTimes.first, intTimes.second),
                        selectedTimeString = "%02d:%02d".format(intTimes.first, intTimes.second),
                        onTimeSelected = viewModel::updateTime,
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Вызываем проверку перед сохранением.
                    requestPermission()
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("Set alarm")
            }
            Button(onMainClick) { Text("On add med page") }
        }
    }
}
