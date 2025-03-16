package com.galeria.medicationstracker.ui.screens.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.R
import com.galeria.medicationstracker.data.MedicationForm
import com.galeria.medicationstracker.data.MedicationUnit
import com.galeria.medicationstracker.ui.components.GDropdownList
import com.galeria.medicationstracker.ui.components.GPrimaryButton
import com.galeria.medicationstracker.ui.components.GTextField
import com.galeria.medicationstracker.ui.componentsOld.FlySimpleCard
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography

@Composable
fun AddMedicationScreen(
    modifier: Modifier = Modifier,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: AddMedicationsViewModel,
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    MedTrackerTheme {
        Scaffold(
            containerColor = MedTrackerTheme.colors.secondaryBackground,
            topBar = {
                Row(modifier = Modifier.padding(vertical = 24.dp)) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Back",
                            tint = MedTrackerTheme.colors.primaryLabel,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                    Text(
                        text = (stringResource(R.string.new_medication)),
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    // Name input.
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            GTextField(
                                value = state.value.medicationName,
                                onValueChange = { viewModel.updateMedicationName(it) },
                                label = "Name",
                                isPrimaryColor = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                modifier = Modifier.weight(1f),
                            )
                            val optionsArray: Array<MedicationForm> =
                                MedicationForm.entries.toTypedArray()
                            val opList: List<String> = optionsArray.map { it.toString() }
                            GDropdownList(items = opList) { selected ->
                                val selectedForm = MedicationForm.valueOf(selected.uppercase())
                                viewModel.updateForm(selectedForm)
                            }
                        }
                    }
                    // Strength.
                    item {
                        var selectedUnit by remember { mutableStateOf(state.value.unit) }
                        val unitOptions = MedicationUnit.entries.toTypedArray()
                        
                        FlySimpleCard(
                            isPrimaryBackground = true,
                            modifier = Modifier.fillMaxWidth(),
                            content = {
                                // Spacer(modifier = Modifier.padding(8.dp))
                                GTextField(
                                    value = state.value.strength.toString(),
                                    onValueChange = { viewModel.updateStrength(it.toFloat()) },
                                    label = stringResource(R.string.medication_strength),
                                    isPrimaryColor = true,
                                    keyboardOptions =
                                        KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.fillMaxWidth(),
                                )
                                val optionsArray: Array<MedicationUnit> =
                                    MedicationUnit.entries.toTypedArray()
                                val opList: List<String> = optionsArray.map { it.toString() }
                                GDropdownList(items = opList) { selected ->
                                    val selectedUnit = MedicationUnit.valueOf(selected.uppercase())
                                    viewModel.updateUnit(selectedUnit)
                                }
                            },
                        )
                        Spacer(modifier = Modifier.padding(16.dp))
                    }
                    // button to add medication.
                    item {
                        val context = LocalContext.current
                        GPrimaryButton(
                            modifier = Modifier.fillMaxWidth(),
                            onClick = {
                                viewModel.addMedication()
                                onConfirmClick.invoke()
                            },
                            content = { Text(text = stringResource(R.string.add_medication)) },
                        )
                    }
                }
            }
        }
    }
}
