package com.galeria.medtracker2.feature.meds.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.core.common.data.MedicationCourseSummary
import java.util.UUID

@Composable
fun MyMedicationsScreen(
    onNavigateToViewMedication: (UUID) -> Unit = {},
    onNavigateToAddMedication: () -> Unit = {},
    viewModel: MyMedicationsVM = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("My medications", style = MaterialTheme.typography.displaySmall) }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Button(
                onClick = onNavigateToAddMedication,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Text("On add med page")
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.medsList.isEmpty()) {
                EmptyMedicationsPlaceholder()
            } else {
                MedsList(medications = state.medsList, onNavigateToViewMedication)
            }
        }
    }
}

@Composable
fun MedsList(medications: List<MedicationCourseSummary>, onMedicationSelect: (UUID) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items = medications, key = { it.medicationId }) { med ->
            MedicationCard(med, onMedicationSelect)
        }
    }
}

@Composable
fun MedicationCard(
    medication: MedicationCourseSummary,
    onSelect: (UUID) -> Unit,
) {
    val formattedStartDate =
            remember(medication.startDate) {
                DateTimeUtils.fromTimestampToDate(medication.startDate)
                    .format(DateTimeUtils.dateFormatter)
            }
    val formattedEndDate =
            remember(medication.endDate) {
                DateTimeUtils.fromTimestampToDate(medication.endDate)
                    .format(DateTimeUtils.dateFormatter)
            }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = { onSelect },
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = medication.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = medication.doseMg.toString(),
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(text = formattedStartDate, style = MaterialTheme.typography.titleLarge)
                Text(text = formattedEndDate, style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

@Composable
private fun EmptyMedicationsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("There's pretty empty.", style = MaterialTheme.typography.bodyLarge)
    }
}
