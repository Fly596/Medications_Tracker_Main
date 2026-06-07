package com.galeria.medtracker2.feature.tracker.presentation.medications

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
                title = {
                    Text(
                        stringResource(R.string.my_medications),
                        style = MedTrackerTheme.typography.display3Emphasized,
                    )
                },
                colors =
                    TopAppBarDefaults.largeTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
            )
        },
        floatingActionButton = {
            // Перенесли добавление лекарств в FAB. Так гораздо удобнее на больших экранах
            ExtendedFloatingActionButton(
                onClick = onNavigateToAddMedication,
                icon = { Icon(Icons.Default.Add, contentDescription = "Add medication icon") },
                text = { Text("Add Medication") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                state.medsList.isEmpty() -> {
                    EmptyMedicationsPlaceholder(
                        onAddClick = onNavigateToAddMedication,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    MedsList(
                        medications = state.medsList,
                        onMedicationSelect = onNavigateToViewMedication,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
        /*  Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else if (state.medsList.isEmpty()) {
                EmptyMedicationsPlaceholder()
            } else {
                MedsList(medications = state.medsList, onNavigateToViewMedication)
            }
        }*/
    }
}

@Composable
fun MedsList(
    medications: List<MedicationCourseSummary>,
    onMedicationSelect: (UUID) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding =
            PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = 88.dp, // Отступ снизу, чтобы FAB не перекрывал последнюю карточку в списке
            ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
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
    modifier: Modifier = Modifier,
) {
    val formattedStartDate =
        remember(medication.startDate) { DateTimeUtils.fromLongToLocalDate(medication.startDate) }
    val formattedEndDate =
        remember(medication.endDate) { DateTimeUtils.fromLongToLocalDate(medication.endDate) }

    Card(
        onClick = { onSelect(medication.medicationId) },
        modifier = modifier.fillMaxWidth(),
        // elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = MedTrackerTheme.shapes.large,
        colors =
            CardDefaults.cardColors(containerColor = MedTrackerTheme.colors.secondaryBackground),
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Иконка-индикатор медицинского препарата для визуального разделения
            Surface(
                shape = MedTrackerTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Healing,
                        contentDescription = "Medication Icon",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = medication.name, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${medication.doseMg.toString()} mg",
                    style = MaterialTheme.typography.titleSmall,
                )
                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "$formattedStartDate - $formattedEndDate",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Composable
private fun EmptyMedicationsPlaceholder(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = "No medications",
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No medications scheduled",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text =
                "Your medication list is currently empty. Tap the button below to add your first medicine.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onAddClick) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Medication")
        }
    }
    /* Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("There's pretty empty.", style = MaterialTheme.typography.bodyLarge)
    }*/
}

@Preview(showBackground = true)
@Composable
fun MedsScreenPreview() {
    MedTrackerTheme {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(4) {
                MedicationCard(
                    medication =
                        MedicationCourseSummary(
                            medicationId = UUID.randomUUID(),
                            name = "Name",
                            doseMg = 50.0,
                            startDate = 0,
                            endDate = 0,
                        ),
                    onSelect = {},
                )
            }
        }
    }
}
