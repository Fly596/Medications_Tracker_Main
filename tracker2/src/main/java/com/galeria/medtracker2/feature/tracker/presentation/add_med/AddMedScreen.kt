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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class AddMedUiState(
    val name: String = "Adderall",
    val dose: String = "50",
    val unit: String = "mg",
    val price: String = "10",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@HiltViewModel
class AddMedVM
@Inject
constructor() : ViewModel() {

    val units = listOf("mg", "g")
    private val _state = MutableStateFlow(AddMedUiState())
    val state = _state.asStateFlow()

    fun updateName(input: String) {
        _state.update { it.copy(name = input) }
    }

    fun updateDose(input: String) {
        // digits only.
        if (input.all { char -> char.isDigit() }) {
            _state.update { it.copy(dose = input) }
        }
    }

    fun updateUnit(input: String) {
        if (input in units) {
            _state.update { it.copy(unit = input) }
        }
    }

    fun updatePrice(input: String) {
        // digits only.
        if (input.all { char -> char.isDigit() }) {
            _state.update { it.copy(price = input) }
        }
    }
}

@Composable
fun AddMedScreen(
    onConfirm: () -> Unit = {},
    onDismiss: () -> Unit = {},
    viewModel: AddMedVM = hiltViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "New Medication") },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
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
            verticalArrangement = Arrangement.Center,
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
                DropDownDemo(items = viewModel.units, onItemSelected = viewModel::updateUnit)
            }
            TextField(
                value = uiState.price,
                suffix = { Text("₽") },
                onValueChange = viewModel::updatePrice,
                label = { Text("Price per g") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
fun DropDownDemo(
    items: List<String>,
    onItemSelected: (String) -> Unit
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
                    Text(text = items[itemPosition.value])
                }
            }

            DropdownMenu(
                expanded = isDropDownExpanded.value,
                onDismissRequest = {
                    isDropDownExpanded.value = false
                }) {
                items.forEachIndexed { index, username ->
                    DropdownMenuItem(
                        text = {
                            Text(text = username)
                        },
                        onClick = {
                            isDropDownExpanded.value = false
                            itemPosition.value = index
                            onItemSelected(username)
                        })
                }
            }
        }
    }
}
