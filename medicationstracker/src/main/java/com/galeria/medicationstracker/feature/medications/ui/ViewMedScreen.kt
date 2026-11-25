package com.galeria.medicationstracker.feature.medications.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.core.ui.components.FlySimpleCard
import com.galeria.medicationstracker.core.ui.components.FlyTonalButton
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.core.ui.theme.MedTrackerTheme.typography
import com.galeria.medicationstracker.domain.medications.model.Medication
import com.galeria.medicationstracker.feature.medications.presentation.MedicationInfoViewModel
import com.galeria.medicationstracker.utils.getTodaysDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationInfoScreen(
    modifier: Modifier = Modifier,
    medicationId: String = "",
    onNavigateToMedsList: () -> Unit = {},
    viewModel: MedicationInfoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(medicationId) {
        if (medicationId.isNotBlank()) {
            viewModel.loadMedicationDetails(medicationId)
        }
    }

    MedTrackerTheme {
        Scaffold(

            modifier = modifier.fillMaxSize(),
            containerColor = MedTrackerTheme.colors.secondaryBackground,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = getTodaysDate().format(
                                DateTimeFormatter.ofPattern(
                                    "MMM d"
                                )
                            ),
                            style = typography.display3Emphasized,
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onNavigateToMedsList,
                            modifier = Modifier.padding(end = 16.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Back",
                                tint = MedTrackerTheme.colors.primaryLabel,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    windowInsets =
                        WindowInsets(
                            top = 0,
                            bottom = 0,
                        ),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MedTrackerTheme.colors.secondaryBackground,
                        navigationIconContentColor = MedTrackerTheme.colors.primaryLabel,
                        titleContentColor = MedTrackerTheme.colors.primaryLabel,
                    )
                )
            },
        ) { innerPadding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // имя и дни приема.
                Text(
                    text = uiState.medication?.name ?: "",
                    style = typography.title1Emphasized,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                //MedInfoHeader(medName = uiState.medication?.name ?: "")
                // начальная дата, total taken/skipped.
                MedStatCard(
                    medication = uiState.medication,
                )
                FlyTonalButton(onClick = onNavigateToMedsList) { Text(text = "Return") }
            }
        }
    }
}

@Composable
fun MedStatCard(
    modifier: Modifier = Modifier,
    //medication: NewUserMedication? = null,
    medication: Medication? = null,
) {
    FlySimpleCard(modifier = modifier) {
        // начальная дата.
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                stringResource(R.string.start_date),
                style = typography.bodyMedium
            )
            Text(
                medication?.startDate.toString(),
                style = typography.title3Emphasized
            )
        }
        HorizontalDivider(
            color = MedTrackerTheme.colors.opaqueSeparator,
            thickness = 0.5.dp
        )

        MedStatCardBody()
    }
}

@Composable
fun MedStatCardBody(totalTaken: Int = 0, totalSkipped: Int = 0) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                stringResource(R.string.total_taken),
                style = typography.bodyMedium
            )
            Text(
                "$totalTaken times",
                style = typography.title2Emphasized
            )
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                stringResource(R.string.total_skipped),
                style = typography.bodyMedium
            )
            Text(
                "$totalSkipped times",
                style = typography.title2Emphasized
            )
        }
    }
}

@Preview(backgroundColor = 0xFFF2F2F7, showBackground = true)
@Composable
fun ViewMedicationInfoScreenPreview() {
    // MedTrackerTheme { ViewMedicationInfoScreen() }
}
