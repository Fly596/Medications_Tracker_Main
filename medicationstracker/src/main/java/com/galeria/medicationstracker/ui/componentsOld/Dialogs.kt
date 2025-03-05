package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.galeria.medicationstracker.data.UserMedication
import com.galeria.medicationstracker.ui.components.GOutlinedButton
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.timeToFirestoreTimestamp
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMedicationTimeDialog(
  medication: UserMedication,
  onDismiss: () -> Unit = {},
  onConfirmation: (String) -> Unit = {},
  onConfirmTime: (Timestamp) -> Unit = {},
  onAddNotes: () -> Unit = {},
) {
  val currentDate = LocalDateTime.now()
  val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a")
  val formattedCurrentDate = currentDate.format(dateFormatter)
  val timeState = rememberTimePickerState(
    is24Hour = false
  )
  val timeSelected by remember { mutableStateOf("") }
  var showDialog by remember { mutableStateOf(false) }
  
  Dialog(onDismissRequest = { onDismiss() }) {
    // Draw a rectangle shape with rounded corners inside the dialog
    Card(
      modifier = Modifier
        .fillMaxWidth(),
      colors = CardDefaults.elevatedCardColors(
        containerColor = MedTrackerTheme.colors.primaryBackgroundGrouped,
        contentColor = MedTrackerTheme.colors.primaryLabel,
      ),
      shape = RoundedCornerShape(16.dp),
    ) {
      Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
      ) {
        Text(
          text = formattedCurrentDate,
          modifier = Modifier.padding(bottom = 16.dp),
          style = MedTrackerTheme.typography.title2Emphasized
        )
        
        LogDialogMedicationCard(
          medicationName = medication.name,
          form = medication.form.toString(),
          strength = medication.strength.toString(),
          intakeTime = medication.intakeTime.toString(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        if (timeSelected.isNotEmpty()) {
          Text(
            text = "Selected Time: $timeSelected",
            style = MedTrackerTheme.typography.bodyLarge
          )
        } else if (timeSelected.isEmpty() && !showDialog) {
          GOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
              showDialog = !showDialog
            }
          ) {
            Text(
              modifier = Modifier,
              text = timeSelected.ifEmpty { "Select time" },
              textAlign = TextAlign.Center,
              style = MedTrackerTheme.typography.labelLarge
            )
          }
        }
        
        if (showDialog) {
          TimeInput(
            state = timeState,
          )
          GOutlinedButton(
            modifier = Modifier
              .fillMaxWidth(),
            onClick = {
              showDialog = false
              onDismiss.invoke()
            },
            isError = true
          ) {
            Text("Cancel")
          }
          GPrimaryButton(
            modifier = Modifier
              .fillMaxWidth(),
            onClick = {
              val timeStamp =
                timeToFirestoreTimestamp(
                  timeState.hour,
                  timeState.minute
                )
              
              onConfirmTime(timeStamp)
              showDialog = false
              onDismiss.invoke()
            }
          ) {
            Text("Confirm")
          }
          
        }
      }
    }
  }
}

@Composable
fun LogDialogMedicationCard(
  medicationName: String = "Adderall",
  form: String = "Tablet",
  strength: String = "50.0",
  intakeTime: String = "2:00 PM",
) {
  Card(
    modifier = Modifier
      .fillMaxWidth(),
    colors = CardDefaults.elevatedCardColors(
      containerColor = MedTrackerTheme.colors.secondaryBackgroundGrouped,
      contentColor = MedTrackerTheme.colors.primaryLabel,
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      // Medication info.
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Medication,
          contentDescription = "Capsule Icon",
          modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
          Text(
            text = medicationName,
            style = MedTrackerTheme.typography.title2Emphasized
          )
          Text(
            text = "$form, $strength",
          )
          Text(
            text = "1 capsule at $intakeTime",
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun DialogWithImagePreview() {
  MedTrackerTheme {
    LogMedicationTimeDialog(
      medication = UserMedication(
        name = "Adderall",
        form = "Tablet",
        strength = 50.0f,
        intakeTime = "2:00 PM"
      ), {}, {})
  }
}