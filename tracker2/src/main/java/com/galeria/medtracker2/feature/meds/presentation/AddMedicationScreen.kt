package com.galeria.medtracker2.feature.meds.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.ui.components.DateSelectionRow
import com.galeria.medtracker2.core.ui.components.TimePickerDialogNew
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

    // Состояние для диалога времени держим на уровне ЭКРАНА, а не внутри кнопки
    var isTimePickerVisible by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("New medication", style = MaterialTheme.typography.displaySmall) }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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

            ClickableReadonlyField(
                label = "Start Date",
                text = DateTimeUtils.formatDatePickerMillis(state.startDateMillis),
                onClick = { /* TODO: Открыть DatePicker для Start Date */ })
            ClickableReadonlyField(
                label = "End Date",
                text = DateTimeUtils.formatDatePickerMillis(state.endDateMillis),
                onClick = { /* TODO: Открыть DatePicker для End Date */ }
            )
            Button(
                onClick = { isTimePickerVisible = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Intake Time")
            }

            // region old Блок выбора дат и времени.
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
                items(items = state.intakeTimesNew) { time ->
                    SuggestionChip(
                        onClick = { viewModel.removeTime(time) },
                        label = { Text(DateTimeUtils.formatLocalTime(time)) }
                    )
                    //                    TimeSelectionButton(
//                        label = "%02d:%02d".format(intTimes.first, intTimes.second),
//                        selectedTimeString = "%02d:%02d".format(intTimes.first, intTimes.second),
//                        onTimeSelected = viewModel::updateTime,
//                    )
                }
            }

            if (isTimePickerVisible) {
                TimePickerDialogNew(
                    onConfirm = { time ->
                        viewModel.addTime(time)
                        isTimePickerVisible = false
                    },
                    onDismiss = { isTimePickerVisible = false }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Вызываем проверку перед сохранением.
                    requestPermission()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("Set alarm")
            }
            Button(onMainClick) { Text("On add med page") }
        }
    }
}

// ПРАВИЛЬНЫЙ способ сделать TextField кликабельным (без костылей с InteractionSource)
@Composable
fun ClickableReadonlyField(
    label: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            // Делаем цвета как у активного поля, чтобы оно не выглядело "выключенным"
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            enabled = false // Важно: отключает фокус и клавиатуру!
        )
        // Невидимый слой поверх поля, который перехватывает клики
        Surface(
            modifier = Modifier
                .matchParentSize()
                .clickable { onClick() },
            color = Color.Transparent
        ) {}
    }
}