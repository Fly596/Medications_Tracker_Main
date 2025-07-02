package com.galeria.medicationstracker.ui.screens.medications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.NewUserMedication
import com.galeria.medicationstracker.ui.components.GFABButton
import com.galeria.medicationstracker.ui.components.GTextButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography

@Composable
fun MedicationsScreen(
    modifier: Modifier = Modifier,
    onAddClick: () -> Unit = {},
    onViewClick: (String) -> Unit = {},
    onEditClick: (String) -> Unit = {},
    medicationsViewModel: MedicationsViewModel = hiltViewModel(),
) {
    val uiState by medicationsViewModel.uiState.collectAsStateWithLifecycle()
    
    MedTrackerTheme {
        Scaffold(
            containerColor = MedTrackerTheme.colors.secondaryBackground,
            topBar = {
                Row(
                    modifier = Modifier.padding(
                        vertical = 24.dp,
                        horizontal = 16.dp
                    )
                ) {
                    // today's date.
                    Text(
                        text = stringResource(R.string.medications),
                        style = typography.display3Emphasized,
                    )
                }
            },
            floatingActionButton = { GFABButton(onClick = { onAddClick.invoke() }) },
        ) { innerPadding ->
            Column(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(uiState.userMedications, key = { it.id }) { med ->
                        FlyElevatedCardMedsList(
                            medication = med,
                            onEditClick = { onEditClick(med.name) },
                            onRemoveMedClick = {
                                medicationsViewModel.deleteMedicationFromFirestore(
                                    med.id.toString()
                                )
                            },
                            onViewMed = { onViewClick(med.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FlyElevatedCardMedsList(
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onRemoveMedClick: () -> Unit,
    onViewMed: () -> Unit,
    medication: NewUserMedication? = null,
    shape: Shape = MedTrackerTheme.shapes.medium,
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onViewMed.invoke() },
        shape = shape,
        elevation =
            CardDefaults.elevatedCardElevation(
                defaultElevation = 0.dp,
                pressedElevation = 8.dp,
                focusedElevation = 10.dp,
            ),
        colors =
            CardDefaults.elevatedCardColors(
                containerColor = MedTrackerTheme.colors.primaryBackground,
                contentColor = MedTrackerTheme.colors.primaryLabel,
                disabledContainerColor = MedTrackerTheme.colors.primaryTinted,
                disabledContentColor = MedTrackerTheme.colors.secondary600,
            ),
    ) {
        Row(
            modifier =
                Modifier
                    // .fillMaxSize()
                    .padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
            ) {
                Text(
                    medication?.name ?: "Med Name",
                    style = typography.headline
                )
                
                Spacer(modifier = Modifier)
                
                Text(
                    medication?.dosage ?: "Med Dosage",
                    style = typography.bodyMedium
                )
                Text(
                    medication?.intakeTime ?: "Intake Time",
                    style = typography.bodyMedium
                )
                Text(
                    medication?.daysOfWeek.toString() ?: "Days of Week",
                    style = typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            Column(Modifier, horizontalAlignment = Alignment.End) {
                GTextButton(onEditClick) { Text("Edit") }
                
                GTextButton(
                    errorButton = true,
                    onClick = { onRemoveMedClick.invoke() },
                    textStyle = typography.labelLargeEmphasized,
                ) {
                    Text("Delete")
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFFF1F1F1, showBackground = true)
@Composable
fun FlyElevatedCardMedsListPreview() {
    MedTrackerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            FlyElevatedCardMedsList(
                onEditClick = { /*TODO*/ },
                onRemoveMedClick = { /*TODO*/ },
                onViewMed = { /*TODO*/ },
            )
        }
    }
}
