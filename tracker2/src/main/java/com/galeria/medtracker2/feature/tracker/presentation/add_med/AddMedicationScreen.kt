package com.galeria.medtracker2.feature.tracker.presentation.add_med

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.components.DatePickerModal
import com.galeria.medtracker2.core.ui.components.TimePickerDialogNew
import com.galeria.medtracker2.core.ui.components.rememberNotificationPermissionHandler
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.utils.DateTimeUtils

@Composable
fun AddMedicationScreen(
    onConfirm: () -> Unit = {},
    viewModel: AddMedicationVM = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Получаем функцию-триггер.
    val requestPermission = rememberNotificationPermissionHandler { isGranted ->
        if (isGranted) {
            viewModel.addMedication()
        }
    }

    // Управление видимостью диалогов на уровне экрана
    var isTimePickerVisible by rememberSaveable { mutableStateOf(false) }
    var showStartDatePicker by rememberSaveable { mutableStateOf(false) }
    var showEndDatePicker by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("New medication", style = MaterialTheme.typography.displaySmall) },
              colors =
                      TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                      ),
            )
        },
    ) { innerPadding ->
      Box(modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)) {
            Column(
              modifier =
                      Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                // region Поля ввода основной информации.
                TextField(
                    value = state.name,
                    onValueChange = viewModel::updateName,
                    label = { Text("Medication name") },
                    leadingIcon = { Icon(Icons.Default.Healing, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                  keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                )
                TextField(
                    value = state.dose,
                    onValueChange = viewModel::updateDose,
                    label = { Text("Medication dose") },
                    leadingIcon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                  keyboardOptions =
                          KeyboardOptions(
                            keyboardType = KeyboardType.Decimal,
                            imeAction = ImeAction.Done,
                          ),
                    singleLine = true,
                )
                // endregion

                // Даты в красивом горизонтальном ряду
                Row(
                    modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    ClickableReadonlyField(
                        label = "Start Date",
                        text = DateTimeUtils.formatLongToLocalDateString(state.startDateMillis),
                        onClick = { showStartDatePicker = true },
                      modifier = Modifier.weight(1f),
                    )
                    ClickableReadonlyField(
                        label = "End Date",
                        text = DateTimeUtils.formatLongToLocalDateString(state.endDateMillis),
                        onClick = { showEndDatePicker = true },
                      modifier = Modifier.weight(1f),
                    )
                }
              HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
              )

                // Секция выбора времени
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Intake Times",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                      color = MaterialTheme.colorScheme.onBackground,
                    )
                    TextButton(
                        onClick = { isTimePickerVisible = true },
                      contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    ) {
                        Icon(
                            Icons.Default.AccessTime,
                            contentDescription = null,
                          modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Time")
                    }
                }

                // Элегантная сетка чипсов на FlowRow (не ломает скролл и переносится сама)
                if (state.intakeTimes.isNotEmpty()) {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                      verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        state.intakeTimes.forEach { time ->
                            InputChip(
                                selected = true,
                                onClick = { viewModel.removeTime(time) },
                                label = { Text(DateTimeUtils.formatLocalTime(time)) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove time",
                                      modifier = Modifier.size(16.dp),
                                    )
                                },
                            )
                        }
                    }
                } else {
                    Text(
                        text = "No intake times added yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline,
                      modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
                Spacer(modifier = Modifier.height(130.dp))

                /*     Button(
                    onClick = { requestPermission() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                ) {
                    Text("Set alarm")
                }
                Button(onConfirm) { Text("On add med page") }*/
            }
            // Фиксированная панель кнопок внизу экрана с размытием или фоном
            Surface(
              modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
                color = MaterialTheme.colorScheme.background,
              tonalElevation = 3.dp,
            ) {
                Column(
                  modifier =
                          Modifier/*.windowInsetsPadding(WindowInsets.safeContent)*/.padding(16.dp),
                  verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Button(
                        onClick = { requestPermission() },
                      modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("Set alarm", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = onConfirm,
                      modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text("On add med page", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
    // Рендер диалогов поверх экрана
    if (showStartDatePicker) {
        DatePickerModal(
            initialMillis = state.startDateMillis,
            onDateSelected = { millis ->
              if (millis != null) viewModel.updateStartDate(millis)
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false },
        )
    }

    if (showEndDatePicker) {
        DatePickerModal(
            initialMillis = state.endDateMillis,
            onDateSelected = { millis ->
              if (millis != null) viewModel.updateEndDate(millis)
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false },
        )
    }

    if (isTimePickerVisible) {
        TimePickerDialogNew(
            onConfirm = { time ->
                viewModel.addTime(time)
                isTimePickerVisible = false
            },
            onDismiss = { isTimePickerVisible = false },
        )
    }
}

@Composable
fun ClickableReadonlyField(
    label: String,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = text,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            trailingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = null,
                  modifier = Modifier.size(18.dp),
                )
            },
            modifier = Modifier.fillMaxWidth(),
            // Делаем цвета как у активного поля, чтобы оно не выглядело "выключенным"
            colors =
                OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
            enabled = false, // Важно: отключает фокус и клавиатуру!
        )
        // Невидимый слой поверх поля, который перехватывает клики
        Surface(
          modifier = Modifier
            .matchParentSize()
            .clickable { onClick() },
            color = Color.Transparent,
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun AddMedsScreenPreview() {
    MedTrackerTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(4) {
                ClickableReadonlyField(
                    label = "Label",
                    text = "text",
                  {},
                )
            }
        }
    }
}
