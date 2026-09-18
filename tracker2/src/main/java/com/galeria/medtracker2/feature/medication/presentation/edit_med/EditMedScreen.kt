package com.galeria.medtracker2.feature.medication.presentation.edit_med

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.WeightUnits
import com.galeria.medtracker2.core.ui.theme.MedTrackerTheme
import com.galeria.medtracker2.feature.medication.presentation.add_med.WeightUnitDropdown

@Composable
fun EditMedScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    viewModel: EditMedVM = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EditMedContent(
        state = state,
        modifier = modifier,
        onChangeName = viewModel::updateName,
        onChangeUnit = viewModel::onUnitSelected,
        onChangePrice = viewModel::updatePrice,
        onUpdateMedication = viewModel::updateMedication,
        onBack = onBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditMedContent(
    state: EditMedUiState,
    modifier: Modifier = Modifier,
    onChangeName: (String) -> Unit = {},
    onChangeUnit: (WeightUnits) -> Unit = {},
    onChangePrice: (String) -> Unit = {},
    onUpdateMedication: () -> Unit,
    onBack: () -> Unit = {},
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Medication",
                        style = MedTrackerTheme.typography.title1Emphasized,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },

                windowInsets =
                        WindowInsets(
                            top = 0,
                            bottom = 0,
                        ),
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = state.name,
                onValueChange = onChangeName,
                label = { Text("Medication Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = state.price,
                    onValueChange = onChangePrice,
                    label = { Text("Price per Unit") },
                    modifier = Modifier.weight(1.6f),
                    singleLine = true,
                )
                WeightUnitDropdown(
                    selectedUnit = state.selectedUnit,
                    onUnitSelected = onChangeUnit,
                    modifier = Modifier.weight(1f)
                )
            }
            Button(
                onClick = onUpdateMedication,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Medication")
            }

        }
    }
}