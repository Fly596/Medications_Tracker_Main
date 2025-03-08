package com.galeria.medicationstracker.ui.screens.dashboard.record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme

@Composable
fun LogsCard(name: String, status: String, date: String, time: String) {
    FlySimpleCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = name,
                    style = MedTrackerTheme.typography.bodyLargeEmphasized,
                    color = MedTrackerTheme.colors.primaryLabel
                )
                Text(
                    text = (if (status == "true") "Taken" else "Not Taken"),
                    style = MedTrackerTheme.typography.title2,
                    color = MedTrackerTheme.colors.secondaryLabel
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = date,
                    style = MedTrackerTheme.typography.title2,
                    color = MedTrackerTheme.colors.primaryLabel
                )
                Text(
                    text = time,
                    style = MedTrackerTheme.typography.title2Emphasized,
                    color = MedTrackerTheme.colors.primaryLabel
                )
            }
        }
    }
}

