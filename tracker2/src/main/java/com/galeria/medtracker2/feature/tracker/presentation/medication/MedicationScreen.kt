package com.galeria.medtracker2.feature.tracker.presentation.medication

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import java.util.UUID

@Composable
fun MedicationScreen(onNavigateBack: () -> Unit = {}, viewModel: MedicationVM = hiltViewModel()) {
    val state by viewModel.uiSt.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Medication Overview",
                        style = MedTrackerTheme.typography.display3Emphasized,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Back to list",
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (val currentState = state) {
                is MedicationUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                is MedicationUiState.Empty -> {
                    EmptyMedicationPlaceholder(
                        onNavigateBack = onNavigateBack,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is MedicationUiState.Success -> {
                    // currentState автоматически скастован к Success, medication гарантированно не
                    // null!
                    MedicationView(
                        medicationCourse = currentState.medication,
                        onDelete = viewModel::deleteMedication,
                    )
                }

                is MedicationUiState.Error -> {
                    Text(
                        text = currentState.message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyMedicationPlaceholder(onNavigateBack: () -> Unit, modifier: Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("There's pretty empty.", style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onNavigateBack) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Medication")
        }
    }
}

@Composable
fun MedicationView(
    medicationCourse: MedicationCourseSummary,
    onDelete: (UUID) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        // TODO add next UI elements:
        // 1. Name of medication
        // 2. Dosage
        // 3. Frequency
        // 4. Start date
        // 5. End date
        // 6. Number of remaining doses
        // 7. Schedule
        // 8. History of taking the medication

        Text(text = medicationCourse.name)
        Text(text = medicationCourse.doseMg.toString())
        Row() {
            Text(text = "Start Date: " + medicationCourse.startDate.toString())
            Text(text = "End Date: " + medicationCourse.endDate.toString())
        }
        Button(onClick = { onDelete(medicationCourse.medicationId) }) {
            Text(text = "Delete")
        }
    }
}
