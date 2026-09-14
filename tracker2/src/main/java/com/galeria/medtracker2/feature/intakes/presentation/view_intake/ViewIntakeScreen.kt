package com.galeria.medtracker2.feature.intakes.presentation.view_intake

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ViewIntakeScreen(
    viewModel: ViewIntakeVM = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
}

@Composable
fun ViewIntakeContent(
    state: ViewIntakeUiState,
    modifier: Modifier = Modifier,
) {
}