package com.galeria.medicationstracker.ui.screens.dashboard.moodtracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography

@Composable
fun MoodTrackerScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: MoodTrackerVM = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    MedTrackerTheme {
        Scaffold(
            containerColor = MedTrackerTheme.colors.secondaryBackground,
            topBar = {
                Row(
                    modifier = Modifier.padding(
                        vertical = 24.dp
                    )
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MedTrackerTheme.colors.primaryLabel,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                    Text(
                        text = (stringResource(R.string.how_are_you_feeling_today)),
                        style = typography.display3Emphasized,
                    )
                }
            },
        ) { innerPadding ->
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Slider(
                    value = uiState.value.mood.toFloat(),
                    onValueChange = { viewModel.updateMood(it.toInt()) },
                    valueRange = 1f..10f,
                    steps = 9,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(text = "${uiState.value.mood}")

                TextField(
                    value = uiState.value.notes ?: "",
                    onValueChange = { viewModel.updateNotes(it) },
                    label = { Text(stringResource(R.string.notes)) },
                )

                GPrimaryButton(onClick = { viewModel.addMood(uiState.value.mood) }) {
                    Text(text = stringResource(R.string.add_mood))
                }
            }
        }
    }
}
