package com.galeria.medtracker2.feature.tracker.presentation.add_med

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medtracker2.core.ui.WeightUnits

@Composable
fun AddMedScreen(
    onConfirm: () -> Unit = {},
    onBack: () -> Unit = {},
    viewModel: AddMedVM = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Medication",
                        style = MaterialTheme.typography.displaySmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew, contentDescription = null
                        )
                    }
                },
                windowInsets =
                        WindowInsets(
                            top = 0,
                            bottom = 0,
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
            TextField(
                value = uiState.name,
                onValueChange = viewModel::updateName,
                label = { Text("Medication name") },
                leadingIcon = { Icon(Icons.Default.Healing, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
            ) {
                TextField(
                    value = uiState.dose,
                    onValueChange = viewModel::updateDose,
                    label = { Text("Medication dose") },
                    leadingIcon = { Icon(Icons.Default.MonitorWeight, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done,
                            ),
                    singleLine = true,
                )
                DropDownDemo(
                    items = WeightUnits.entries,
                    selectedItem = uiState.selectedUnit,
                    onItemSelected = viewModel::onUnitSelected,
                )
            }
            TextField(
                value = uiState.price,
                suffix = { Text("₽") },
                onValueChange = viewModel::updatePrice,
                label = { Text("Price per g") },
                //keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth(),
            )
            Button(onClick = viewModel::addMedication) {
                Text("Add Medication")
            }
        }
    }
}

@Composable
fun DropDownDemo(
    items: List<WeightUnits>,
    selectedItem: WeightUnits,
    onItemSelected: (WeightUnits) -> Unit,
) {
    val isDropDownExpanded = remember {
        mutableStateOf(false)
    }
    val itemPosition = remember {
        mutableIntStateOf(0)
    }


    Column(
        modifier = Modifier.padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box {
            OutlinedCard(
                modifier = Modifier,
                onClick = {
                    isDropDownExpanded.value = true
                }
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = stringResource(selectedItem.label))
                }
            }

            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                items.forEachIndexed { index, units ->
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(units.label))
                        },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.intValue = index
                            onItemSelected(units)
                        })
                }
            }
        }
    }
}
