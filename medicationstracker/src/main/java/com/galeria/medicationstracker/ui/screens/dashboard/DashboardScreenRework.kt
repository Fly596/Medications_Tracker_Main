package com.galeria.medicationstracker.ui.screens.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.galeria.medicationstracker.ui.componentsOld.FLySimpleCardContainer
import com.galeria.medicationstracker.ui.componentsOld.WeeklyCalendarView
import com.galeria.medicationstracker.ui.theme.MedTrackerTheme.typography

@Composable
fun DashboardScreenRework(
    modifier: Modifier = Modifier,
    viewModel: DashboardVM,
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val medicationsByIntakeTime =
        viewModel.groupMedicationsByIntakeTime(state.value.currentTakenMedications)
    
    Scaffold(
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = state.value.todayDate)
            }
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .padding(innerPadding)
            .fillMaxWidth()) {
            WeeklyCalendarView()
            
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                medicationsByIntakeTime.forEach { (intakeTime, medications) ->
                    item {
                        FLySimpleCardContainer {
                            Column {
                                Text(
                                    text = intakeTime,
                                    style = typography.title1Emphasized
                                )
                                
                                medications.forEach { medication ->
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}