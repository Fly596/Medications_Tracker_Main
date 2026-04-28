package com.galeria.medtracker2.feature.intakes.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import com.galeria.medtracker2.feature.intakes.domain.IntakeDomain
import com.galeria.medtracker2.feature.intakes.domain.IntakesRepository
import com.galeria.medtracker2.feature.meds.data.local.schedule.FullSchedule
import com.galeria.medtracker2.feature.meds.data.local.schedule.RegimentWithNameDoseDate
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimenRepo
import com.galeria.medtracker2.feature.meds.domain.MedicationRegimentDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
fun MainIntakesScreen(onAddMedClick: () -> Unit = {}, viewModel: MainIntakesVM = hiltViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Button(onAddMedClick) { Text("On add med page") }

        // Список медикаментов.
        /*        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    item { Text("My medications:") }
                    items(items = state.regsUnited) { medRegiment ->
                        Card(modifier = Modifier.padding(vertical = 4.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = medRegiment.name)
                                Text(text = medRegiment.doseMg.toString())
                                Text(
                                    text =
                                        DateTimeUtils.fromTimestampToLocalDate(medRegiment.startDate)
                                            .format(DateTimeUtils.dateFormatter)
                                )
                            }
                        }
                    }
                }*/
        // Полный список приемов.
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item { Text("My intakes") }
            items(items = state.completeSchedule) { regiment ->
                Card(modifier = Modifier.padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = regiment.name)
                        Text(text = regiment.doseMg.toString())
                        Text(
                            text =
                                    DateTimeUtils.fromTimestampToLocalDateTime(regiment.scheduledIntakeDateTime)
                                        .format(DateTimeUtils.dateTimeFormatter)
                        )
                    }
                }
            }
        }
    }
}

data class MainIntakesUiState(
    val intakesList: List<IntakeDomain> = emptyList(),
    val regimentsList: List<MedicationRegimentDomain> = emptyList(),
    val regsUnited: List<RegimentWithNameDoseDate> = emptyList(),
    val completeSchedule: List<FullSchedule> = emptyList(),
)

@HiltViewModel
class MainIntakesVM
@Inject
constructor(
    private val intakesRepository: IntakesRepository,
    private val regimentsRepository: MedicationRegimenRepo,
) : ViewModel() {

    private val _state = MutableStateFlow(MainIntakesUiState())
    val state = _state.asStateFlow()

    init {
        getRegimentsSchedule()
    }

    private fun getRegimentsSchedule() {
        viewModelScope.launch {
            regimentsRepository.getFullSchedule().collect { regs ->
                _state.update { it.copy(completeSchedule = regs) }
            }

            regimentsRepository.getRegimentsWithNameDoseDates().collect { regs ->
                _state.update { it.copy(regsUnited = regs) }
            }
            regimentsRepository.getRegiments().collect { regiments ->
                _state.update { it.copy(regimentsList = regiments) }
            }
        }
    }
}
