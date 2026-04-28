package com.galeria.medtracker2.feature.intakes.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.common.DateTimeUtils
import com.galeria.medtracker2.feature.meds.data.local.schedule.FullSchedule
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun MainIntakesScreen(
    onAddMedicationClick: () -> Unit = {},
    viewModel: MainIntakesVM = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Button(
            onClick = onAddMedicationClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text("On add med page")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ближайшие приемы",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp),
        )

        if (state.plannedIntakes.isEmpty() && !state.isLoading) {
            EmptySchedulePlaceholder()
        } else {
            IntakeList(state.plannedIntakes)
        }

        // Полный список приемов.

    }
}

@Composable
fun IntakeList(intakes: List<FullSchedule>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(items = intakes, key = { it.id }) { intake -> IntakeCard(intake) }
    }
}

@Composable
fun IntakeCard(intake: FullSchedule) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = intake.name, style = MaterialTheme.typography.titleLarge)
                Text(
                    text = "${intake.doseMg} mg",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Форматирование даты и времени
            val formattedTime =
                    DateTimeUtils.fromTimestampToLocalDateTime(intake.scheduledIntakeDateTime)
                        .format(DateTimeUtils.dateTimeFormatter)

            Text(
                text = formattedTime,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun EmptySchedulePlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("На сегодня приемов нет", style = MaterialTheme.typography.bodyLarge)
    }
}

data class ScheduleUiState(
    val plannedIntakes: List<FullSchedule> = emptyList(),
    val isLoading: Boolean = true,
)

@HiltViewModel
class MainIntakesVM
@Inject
constructor(
    private val regimentsRepository: MedicationRegimenRepo,
) : ViewModel() {

    private val _state = MutableStateFlow(ScheduleUiState())
    val state = _state.asStateFlow()

    init {
        loadSchedule()
    }

    private fun loadSchedule() {
        viewModelScope.launch {
            regimentsRepository.getFullSchedule().distinctUntilChanged().collect { schedule ->
                _state.update { it.copy(plannedIntakes = schedule, isLoading = false) }
            }
        }
    }
}
