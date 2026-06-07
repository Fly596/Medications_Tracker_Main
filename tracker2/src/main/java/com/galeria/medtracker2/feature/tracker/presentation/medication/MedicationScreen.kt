package com.galeria.medtracker2.feature.tracker.presentation.medication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.R
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.MedicationCourseSummary
import java.util.UUID

@Composable
fun MedicationScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: MedicationVM = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.medication),style = MedTrackerTheme.typography.display3Emphasized)
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Back to list"
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ){innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                state.medication == null -> {
                    EmptyMedicationPlaceholder(
                        onAddClick = onNavigateBack,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    MedicationView(medication = state.medication)
                }
            }
        }

    }
}

@Composable
fun MedicationView(medicationCourse: MedicationCourseSummary,   
                modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ){
        // TODO add next UI elements:
        // 1. Name of medication
        // 2. Dosage
        // 3. Frequency
        // 4. Start date
        // 5. End date
        // 6. Number of remaining doses
        // 7. Schedule
        // 8. History of taking the medication

        Text(text = medicationCourse.medicationName)
        Text(text = medicationCourse.dosage)
        Text(text = medicationCourse.frequency)
        Row(){
            Text(text = "Start Date: " + medicationCourse.startDate.toString())
            Text(text = "End Date: " + medicationCourse.endDate.toString())
        }
        Text(text = "Remaining Doses: " + medicationCourse.remainingDoses.toString())
        Text(text = "Schedule: " + medicationCourse.schedule.toString())
        Text(text = medicationCourse.history.toString())
    }
}