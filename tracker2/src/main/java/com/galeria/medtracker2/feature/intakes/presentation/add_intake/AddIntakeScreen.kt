package com.galeria.medtracker2.feature.intakes.presentation.add_intake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddIntakeScreen(

    onNavigateBack: () -> Unit = {},
    onAddIntake: () -> Unit = {},
    viewModel: AddIntakeVM = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AddIntakeContent(
        uiState = uiState,
        onDosageChange = viewModel::updateDosage,
        onTimeChange = viewModel::updateTime,
        onDateChange = viewModel::updateDate,
        onAddIntake = onAddIntake,
        modifier = Modifier
    )
}

@Composable
fun AddIntakeContent(
    uiState: AddIntakeUiState,
    onDosageChange: (String) -> Unit,
    onTimeChange: (String) -> Unit,
    onDateChange: (String) -> Unit,
    onAddIntake: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("New medication", style = MaterialTheme.typography.displaySmall) },
                colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}