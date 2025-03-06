package com.galeria.medicationstracker.ui.screens.profile.appoinment

import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.ui.componentsOld.FlyButton
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.componentsOld.FlyTextButton
import com.galeria.medicationstracker.ui.componentsOld.FlyTonalButton
import com.galeria.medicationstracker.ui.screens.profile.ProfileVM
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
  modifier: Modifier = Modifier,
  viewModel: ProfileVM = viewModel(),
  onBackClick: () -> Unit = {}
) {
  val uiState = viewModel.uiState.collectAsStateWithLifecycle()
  var selectedDate by remember { mutableStateOf<Long?>(null) }
  
  Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.Start
  ) {
    Text(
      text = "Schedule Your Appointment",
      style = MaterialTheme.typography.titleLarge,
      modifier = Modifier.padding(bottom = 24.dp)
    )
    
    FlySimpleCard(
      modifier = Modifier.fillMaxWidth(),
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
      ) {
        Text(
          text = "Date:",
          style = MedTrackerTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        if (selectedDate != null) {
          val date = Date(selectedDate!!)
          val formattedDate =
            SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
          Text(
            formattedDate,
            style = MedTrackerTheme.typography.title3Emphasized,
          )
        } else {
          Text("No date selected")
        }
        var showDatePicker by remember { mutableStateOf(false) }
        
        FlyTonalButton(
          onClick = { showDatePicker = true },
          shape = MaterialTheme.shapes.small
        ) {
          Text("Select Date")
        }
      }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    FlySimpleCard(
      modifier = Modifier.fillMaxWidth(),
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
      ) {
        // Display selected date
        var showTimePicker by remember { mutableStateOf(false) }
        var selectedTimeState: TimePickerState? by remember {
          mutableStateOf(
            null
          )
        }
        val timeFormatter =
          remember { SimpleDateFormat("hh:mm a", Locale.getDefault()) }
        Text(
          text = "Time:",
          style = MedTrackerTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        
        if (selectedTimeState != null) {
          val cal = Calendar.getInstance()
          
          cal.set(Calendar.HOUR_OF_DAY, selectedTimeState!!.hour)
          cal.set(Calendar.MINUTE, selectedTimeState!!.minute)
          cal.isLenient = false
          Text(
            timeFormatter.format(cal.time),
            style = MedTrackerTheme.typography.title3Emphasized,
          )
        } else {
          Text(
            "No time selected.",
            style = MedTrackerTheme.typography.bodyMedium,
          )
        }
        FlyTonalButton(
          onClick = {
            showTimePicker = true
          },
          shape = MaterialTheme.shapes.small
        ) {
          Text("Select Time")
        }
      }
    }
    
    Spacer(modifier = Modifier.height(16.dp))
    
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      FlyButton(
        onClick = {
          // viewModel.addAppointment()
          onBackClick.invoke()
        },
        shape = MaterialTheme.shapes.small
      ) {
        Text(
          text = "Confirm Appointment",
        )
      }
      FlyTextButton(
        onClick = {
          /* TODO: Add logic to cancel appointment */
        },
      ) {
        Text(
          text = "Cancel",
        )
      }
    }
  }
  
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalInput(
  onDateSelected: (Long?) -> Unit,
  onDismiss: () -> Unit
) {
  val datePickerState =
    rememberDatePickerState(initialDisplayMode = DisplayMode.Input)
  
  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(onClick = {
        onDateSelected(datePickerState.selectedDateMillis)
        onDismiss()
      }) {
        Text("OK")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancel")
      }
    }
  ) {
    DatePicker(state = datePickerState)
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputExample(
  onConfirm: (TimePickerState) -> Unit,
  onDismiss: () -> Unit,
) {
  val currentTime = Calendar.getInstance()
  val timePickerState = rememberTimePickerState(
    initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
    initialMinute = currentTime.get(Calendar.MINUTE),
    is24Hour = true,
  )
  
  Column {
    TimeInput(
      state = timePickerState,
    )
    Button(onClick = onDismiss) {
      Text("Dismiss picker")
    }
    Button(onClick = {
      onConfirm(timePickerState)
    }) {
      Text("Confirm selection")
    }
  }
}
