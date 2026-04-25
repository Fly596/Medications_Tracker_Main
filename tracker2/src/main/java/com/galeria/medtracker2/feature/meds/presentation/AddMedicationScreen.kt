package com.galeria.medtracker2.feature.meds.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AddMedicationScreen(viewModel: AddMedicationVM = hiltViewModel()) {
    val state = viewModel.state.collectAsStateWithLifecycle()
}

