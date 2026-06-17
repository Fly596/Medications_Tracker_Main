package com.galeria.medtracker2.feature.tracker.presentation.edit_medication

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun EditMedicationScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: EditMedicationVM = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
}
