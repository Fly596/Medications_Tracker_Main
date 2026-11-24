package com.galeria.medicationstracker.ui.componentsOld

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme

@Composable
fun InputDialog(
    modifier: Modifier = Modifier, title: String = "Title",
    viewModel: InputDialogViewModel = viewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    FlySimpleCard(
        isPrimaryBackground = false
    ) {

        // cancel/name/confirm.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FlyTextButton(
                onClick = {/* TODO */ }
            ) {
                Text(
                    text = "Cancel",
                    style = MedTrackerTheme.typography.bodyMedium
                )
            }

            Text(text = title, style = MedTrackerTheme.typography.title2Emphasized)

            FlyTextButton(
                onClick = {/* TODO */ }
            ) {
                Text(
                    text = "Add",
                    style = MedTrackerTheme.typography.bodyMediumEmphasized
                )
            }

        }

        MyTextField(
            value = uiState.value.dateTimeInput.toString(),
            onValueChange = {/*
            TODO: Add date picker.
             */
            },
            label = "Date",
            modifier = Modifier.fillMaxWidth()
        )

        MyTextField(
            value = uiState.value.measurementInput,
            onValueChange = {/* TODO */ },
            label = title,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
fun InputDialogPreview() {
    MedTrackerTheme {
        InputDialog(modifier = Modifier)
    }
}