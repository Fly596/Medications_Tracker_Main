package com.galeria.medtracker2.feature.medication.presentation.view_med

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.MedicationDomain
import java.time.Instant
import java.time.ZoneId
import java.util.UUID

/**
 * Stateful entry point for the View Medication screen.
 * Collects UI state from [ViewMedVM] and delegates UI rendering to stateless [ViewMedContent].
 */
@Composable
fun ViewMedScreen(
    onNavigateBack: () -> Unit = {},
    onEditMedication: (UUID) -> Unit = {},
    viewModel: ViewMedVM = hiltViewModel(),
    onAddIntake: (UUID) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ViewMedContent(
        state = state,
        onNavigateBack = onNavigateBack,
        onEditMedication = onEditMedication,
        onDeleteMedication = { id ->
            viewModel.deleteMedication(id)
            onNavigateBack()
        },
        onAddIntake = onAddIntake
    )
}

/**
 * Stateless content composable for displaying medication details according to [ViewMedUiState].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMedContent(
    state: ViewMedUiState,
    onNavigateBack: () -> Unit,
    onEditMedication: (UUID) -> Unit,
    modifier: Modifier = Modifier,
    onDeleteMedication: (UUID) -> Unit = {},
    onAddIntake: (UUID) -> Unit = {}
) {
    val topBarTitle = when (state) {
        is ViewMedUiState.Success -> state.medication.name
        else -> "Medication Details"
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = topBarTitle,
                        style = MedTrackerTheme.typography.title1Emphasized,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Navigate back",
                        )
                    }
                },
                actions = {
                    if (state is ViewMedUiState.Success) {
                        IconButton(onClick = { onEditMedication(state.medication.id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit medication",
                            )
                        }
                        IconButton(onClick = { onDeleteMedication(state.medication.id) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete medication",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            if (state is ViewMedUiState.Success) {
                FloatingActionButton(
                    onClick = { onAddIntake(state.medication.id) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(16.dp)
                ) { }
            }

        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (state) {
                is ViewMedUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                is ViewMedUiState.Empty -> {
                    EmptyMedicationPlaceholder(
                        onNavigateBack = onNavigateBack,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                is ViewMedUiState.Success -> {
                    MedicationOverview(
                        medication = state.medication
                    )
                }

                is ViewMedUiState.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Scrollable overview section displaying medication metadata cards and activity log.
 */
@Composable
fun MedicationOverview(
    medication: MedicationDomain,
    modifier: Modifier = Modifier
) {
    val formattedDate = remember(medication.creationTimestamp) {
        DateTimeUtils.formatLocalDate(
            medication.creationTimestamp.atZone(ZoneId.systemDefault()).toLocalDate()
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Summary Cards Section
        item(key = "summary_section") {
            MedicationSummary(
                unit = medication.unit.name,
                pricing = medication.defaultPricePerUnit?.cents ?: 0,
                createdDate = formattedDate
            )
        }

        // Header for Recent Intakes
        item(key = "recent_activity_header") {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // Intake History List
        items(
            count = 10,
            key = { index -> "intake_item_$index" }
        ) { index ->
            IntakeCard(
                unit = medication.unit.name,
                index = index
            )
        }
    }
}

/**
 * Summary grid showcasing core metadata attributes of the medication.
 */
@Composable
fun MedicationSummary(
    unit: String,
    pricing: Long,
    createdDate: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                label = "Unit / Dosage",
                value = unit.ifEmpty { "N/A" },
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                label = "Price",
                value = "$$pricing",
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SummaryCard(
                label = "Date Added",
                value = createdDate,
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                label = "Status",
                value = "Active",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun SummaryCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun IntakeCard(
    unit: String,
    index: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Intake #${index + 1}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "1 x ${unit.ifEmpty { "dose" }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = "Today",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
fun EmptyMedicationPlaceholder(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(72.dp),
            tint = MaterialTheme.colorScheme.outlineVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No Medication Found",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "This prescription or course has run out or does not exist.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateBack) {
            Text("Go Back")
        }
    }
}

// ============================================================================
// Previews
// ============================================================================

@Preview(showBackground = true)
@Composable
private fun ViewMedContentSuccessPreview() {
    MedTrackerTheme {
        ViewMedContent(
            state = ViewMedUiState.Success(
                medication = MedicationDomain(
                    id = UUID.randomUUID(),
                    name = "Aspirin 500mg",
                    unit = WeightUnits.DEFAULT,
                    defaultPricePerUnit = null,
                    creationTimestamp = Instant.now()
                )
            ),
            onNavigateBack = {},
            onEditMedication = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewMedContentEmptyPreview() {
    MedTrackerTheme {
        ViewMedContent(
            state = ViewMedUiState.Empty,
            onNavigateBack = {},
            onEditMedication = {}
        )
    }
}
