package com.galeria.medicationstracker.ui.screens.medications.mediinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.network.NetworkMedication
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.componentsOld.FlyTonalButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography
import com.galeria.medicationstracker.utils.getTodaysDate
import java.time.format.DateTimeFormatter

@Composable
fun ViewMedicationInfoScreen(
    modifier: Modifier = Modifier,
    onNavigateToMedsList: () -> Unit = {},
    viewModel: ViewMedicationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
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
                    IconButton(
                        onClick = onNavigateToMedsList,
                        modifier = Modifier.padding(end = 16.dp),
                    ) {
                    }
                    // today's date.
                    Text(
                        text = getTodaysDate().format(
                            DateTimeFormatter.ofPattern(
                                "MMM d"
                            )
                        ),
                        style = typography.display3Emphasized,
                    )
                }
            },
        ) { innerPadding ->
            Column(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(innerPadding)
                        .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // имя и дни приема.
                MedInfoHeader(medName = uiState.medication?.name ?: "")
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
    medication: NetworkMedication? = null,
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

@Composable
fun MedInfoHeader(
    modifier: Modifier = Modifier,
    medName: String = "Doxycycline"
) {
    Column(modifier.padding(vertical = 16.dp)) {
        Text(
            text = medName,
            style = typography.title1Emphasized
        )
    }
}

@Preview(backgroundColor = 0xFFF2F2F7, showBackground = true)
@Composable
fun ViewMedicationInfoScreenPreview() {
    // MedTrackerTheme { ViewMedicationInfoScreen() }
}
