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
import com.galeria.medicationstracker.ui.components.GOutlinedButton
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.utils.timeToFirestoreTimestamp
import com.google.firebase.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirmation: () -> Unit,
    message: String,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MedTrackerTheme.colors.primaryBackgroundGrouped,
                contentColor = MedTrackerTheme.colors.primaryLabel,
            ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Text(
                text = message,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                style = MedTrackerTheme.typography.title3
            )
        }
    }
    
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogMedicationTimeDialog(
    onDismiss: () -> Unit = {},
    onConfirmation: (String) -> Unit = {},
    onConfirmTime: (Timestamp) -> Unit = {},
    onAddNotes: () -> Unit = {},
    onSkipIntake: () -> Unit = {},
) {
    val currentDate = LocalDateTime.now()
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, hh:mm a")
    val formattedCurrentDate = currentDate.format(dateFormatter)
    val timeState = rememberTimePickerState(
        is24Hour = false
    )
    var selectedTime: TimePickerState? by remember { mutableStateOf(null) }
    
    var timeSelected by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    
    Dialog(onDismissRequest = { onDismiss() }) {
        // Draw a rectangle shape with rounded corners inside the dialog
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                    text = "$formattedCurrentDate",
                    modifier = Modifier.padding(16.dp),
                    style = MedTrackerTheme.typography.title2Emphasized
                )
                
                LogDialogMedicationCard(
                )
                Spacer(modifier = Modifier.height(16.dp))
                GPrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        showDialog = !showDialog
                    }
                ) {
                    Text(
                        modifier = Modifier,
                        text = if (timeSelected.isEmpty()) "Select time" else timeSelected,
                        textAlign = TextAlign.Center,
                        style = MedTrackerTheme.typography.labelLarge
                    )
                }
                GOutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSkipIntake.invoke()
                    }
                ) {
                    Text(
                        modifier = Modifier,
                        text = "Skip intake",
                        textAlign = TextAlign.Center,
                        style = MedTrackerTheme.typography.labelLarge
                    )
                }
                if (showDialog) {
                    TimeInput(
                        state = timeState,
                    )
                    GOutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
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
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
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
                /*                 GPrimaryButton(
                                    modifier = Modifier.fillMaxWidth(),
                                    onClick = { onConfirmation.invoke(timeSelected) }, // Pass the selected time
                                ) {
                                    Text("Confirm")
                                } */
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
    onSkipped: () -> Unit = {},
    onTaken: () -> Unit = {},
    dosageValues: List<Float> = emptyList(),
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
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = medicationName,
                    )
                    Text(
                        text = "$form, $strength",
                    )
                    Text(
                        text = "1 capsule at $intakeTime",
                    )
                }
            }
            /*        Spacer(modifier = Modifier.height(16.dp))
                   // Buttons.
                   Row(
                       horizontalArrangement = Arrangement.spacedBy(12.dp),
                       modifier = Modifier.fillMaxWidth()
                   ) {
                       GTonalButton(
                           modifier = Modifier.weight(0.5f),
                           onClick = onSkipped,
                       ) {
                           Text(text = "Skipped")
                       }
                       GTonalButton(
                           modifier = Modifier.weight(0.5f),
                           onClick = onTaken,
                       ) {
                           Text(text = "Taken")
                       }
                   } */
        }
    }
}

@Preview
@Composable
fun DialogWithImagePreview() {
    MedTrackerTheme {
        LogMedicationTimeDialog({}, {})
    }
}