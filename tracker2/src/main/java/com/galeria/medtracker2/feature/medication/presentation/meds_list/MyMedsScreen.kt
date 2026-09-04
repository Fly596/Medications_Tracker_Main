package com.galeria.medtracker2.feature.medication.presentation.meds_list

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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.R
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.domain.model.MedicationDomain
import java.time.Instant
import java.util.UUID

/**
 * Stateful entry point (Route) for the "My Medications" screen.
 * Handles ViewModel injection, State Flow collection with lifecycle awareness, and event delegation.
 */
@Composable
fun MyMedsScreen(
    modifier: Modifier = Modifier,
    onNavigateToViewMedication: (UUID) -> Unit = {},
    onNavigateToAddMedication: () -> Unit = {},
    viewModel: MyMedsVM = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    MyMedsContent(
        state = state,
        onNavigateToViewMedication = onNavigateToViewMedication,
        onNavigateToAddMedication = onNavigateToAddMedication,
        modifier = modifier,
    )
}

/**
 * Stateless UI container for the "My Medications" screen.
 * Separated from ViewModel for easy previewing, unit testing, and UI reuse.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMedsContent(
    state: MyMedsUiState,
    modifier: Modifier = Modifier,
    onNavigateToViewMedication: (UUID) -> Unit = {},
    onNavigateToAddMedication: () -> Unit = {},
) {
    // Scroll behavior for top app bar elevation / hiding on scroll
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val lazyListState = rememberLazyListState()

    // Collapse FAB when scrolling down the list to optimize screen real estate
    val isFabExpanded by remember {
        derivedStateOf {
            lazyListState.firstVisibleItemIndex == 0 &&
                    lazyListState.firstVisibleItemScrollOffset < 10
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_medications),
                        style = MedTrackerTheme.typography.title1Emphasized,
                    )
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                ),
            )
        },
        floatingActionButton = {
            // Show FAB only when medications exist to avoid duplicating the empty screen's primary CTA
            if (!state.isLoading && state.medications.isNotEmpty()) {
                Row(
                    modifier = Modifier.width(250.dp),
                ) {
                    FloatingActionButton(
                        onClick = onNavigateToAddMedication,
                        modifier = Modifier.weight(1f),
                        containerColor = MedTrackerTheme.colors.secondary400,
                        contentColor = MedTrackerTheme.colors.primaryLabelDark,
                    ) {
                        Text(stringResource(R.string.add_intake))
                    }
                    FloatingActionButton(
                        onClick = onNavigateToAddMedication,
                        modifier = Modifier.weight(1f),
                        containerColor = MedTrackerTheme.colors.primary400,
                        contentColor = MedTrackerTheme.colors.primaryLabelDark,
                    ) {
                        Text(stringResource(R.string.add_medication))
                    }
                    /*   ExtendedFloatingActionButton(
                           onClick = onNavigateToAddMedication,
                           expanded = isFabExpanded,
                           icon = {
                               Icon(
                                   imageVector = Icons.Default.Add,
                                   contentDescription = null, // Set null as text handles accessibility
                               )
                           },
                           text = { Text(stringResource(R.string.add_intake)) },
                           containerColor = MaterialTheme.colorScheme.primary,
                           contentColor = MaterialTheme.colorScheme.onPrimary,
                           modifier = Modifier.weight(1f)
                       )*/
                    /*ExtendedFloatingActionButton(
                        onClick = onNavigateToAddMedication,
                        expanded = isFabExpanded,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null, // Set null as text handles accessibility
                            )
                        },
                        text = { Text(stringResource(R.string.add_medication)) },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.weight(1f)
                    )*/
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                state.medications.isEmpty() -> {
                    EmptyMedicationsPlaceholder(
                        onAddMedClick = onNavigateToAddMedication,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    MedsList(
                        medications = state.medications,
                        onMedicationSelect = onNavigateToViewMedication,
                        lazyListState = lazyListState,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}

/**
 * Lazy list component displaying medication cards with item animations and keying.
 */
@Composable
fun MedsList(
    medications: List<MedicationDomain>,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState(),
    onMedicationSelect: (UUID) -> Unit = {},
) {
    LazyColumn(
        state = lazyListState,
        modifier = modifier,
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = 96.dp, // Extra padding at bottom so FAB doesn't obscure the last card
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(
            items = medications,
            key = { it.id },
        ) { med ->
            MedicationCard(
                medication = med,
                onSelect = onMedicationSelect,
                modifier = Modifier.animateItem(), // Smooth insertion/deletion animation
            )
        }
    }
}

/**
 * Individual medication card displaying medication name and dosage info.
 */
@Composable
fun MedicationCard(
    medication: MedicationDomain,
    modifier: Modifier = Modifier,
    onSelect: (UUID) -> Unit = {},
) {
    Card(
        onClick = { onSelect(medication.id) },
        modifier = modifier.fillMaxWidth(),
        shape = MedTrackerTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MedTrackerTheme.colors.secondaryBackground
        ),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = medication.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Dosage / last consumed badge
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.wrapContentSize(),
                ) {
                    Text(
                        text = stringResource(R.string.last_consumed_placeholder),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    )
                }
            }
            // Navigation indicator arrow
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null, // Decorative icon for card navigation
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp),
            )
        }
    }
}

/**
 * Placeholder layout displayed when no medications have been added yet.
 */
@Composable
private fun EmptyMedicationsPlaceholder(
    modifier: Modifier = Modifier,
    onAddMedClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            imageVector = Icons.Default.Inbox,
            contentDescription = stringResource(R.string.cd_no_medications),
            tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
            modifier = Modifier.size(72.dp),
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = stringResource(R.string.no_medications_scheduled),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.empty_medications_description),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onAddMedClick,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.add_medication),
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// region Previews

@Preview(showBackground = true)
@Composable
private fun MyMedsScreenContentPreview() {
    MedTrackerTheme {
        MyMedsContent(
            state = MyMedsUiState(
                isLoading = false,
                medications = listOf(
                    MedicationDomain(
                        id = UUID.randomUUID(),
                        name = "Aspirin 100mg",
                        pricing = 10,
                        unit = "mg",
                        creationTimestamp = Instant.now(),
                    ),
                    MedicationDomain(
                        id = UUID.randomUUID(),
                        name = "Ibuprofen 200mg",
                        pricing = 15,
                        unit = "mg",
                        creationTimestamp = Instant.now(),
                    ),
                ),
            ),
            onNavigateToViewMedication = {},
            onNavigateToAddMedication = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyMedsScreenEmptyPreview() {
    MedTrackerTheme {
        MyMedsContent(
            state = MyMedsUiState(
                isLoading = false,
                medications = emptyList(),
            ),
            onNavigateToViewMedication = {},
            onNavigateToAddMedication = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun MyMedsScreenLoadingPreview() {
    MedTrackerTheme {
        MyMedsContent(
            state = MyMedsUiState(
                isLoading = true,
                medications = emptyList(),
            ),
            onNavigateToViewMedication = {},
            onNavigateToAddMedication = {},
        )
    }
}

// endregion
