package com.galeria.medtracker2.feature.meds.presentation

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.notification.ScheduleNotification
import com.galeria.medtracker2.core.ui.components.DateSelectionRow
import com.galeria.medtracker2.core.ui.components.TimeSelectionRow

@Composable
fun AddMedicationScreen(viewModel: AddMedicationVM = hiltViewModel()) {
    val context = LocalContext.current
    val state = viewModel.state.collectAsStateWithLifecycle()

    val date = remember { Calendar.getInstance().timeInMillis }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        TextField(
            value = state.value.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Medication name") },
            singleLine = true,
        )
        TextField(
            value = state.value.dose,
            onValueChange = { viewModel.updateDose(it) },
            label = { Text("Medication dose") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        )

        DateSelectionRow(
            label = "Start Date",
            selectedDateString = state.value.startDate,
            onDateSelected = { viewModel.updateStartDate(it) },
        )
        DateSelectionRow(
            label = "End Date",
            selectedDateString = state.value.endDate,
            onDateSelected = { viewModel.updateEndDate(it) },
        )
        TimeSelectionRow(
            label = "Time",
            selectedTimeString = state.value.selectedTime,
            onTimeSelected = { viewModel.updateTime(it) },
        )
        Button(
            onClick = {
                ScheduleNotification()
                    .scheduleNotification(
                        context = context,
                        timePickerState =
                                TimePickerState(
                                    state.value.selectedTimeInt.first,
                                    state.value.selectedTimeInt.second,
                                    false,
                                ),
                        datePickerState =
                                DatePickerState(
                                    locale = CalendarLocale.getDefault(),
                                    initialSelectedDateMillis = state.value.startDateLong,
                                ),
                        title = "",
                    )
            }
        ) {
            Text("Set alarm")
        }
    }
}
