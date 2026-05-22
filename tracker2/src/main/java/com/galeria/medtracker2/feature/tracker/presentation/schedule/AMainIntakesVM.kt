package com.galeria.medtracker2.feature.tracker.presentation.schedule

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.core.utils.DateTimeUtils
import com.galeria.medtracker2.domain.model.IntakeLogDomain
import com.galeria.medtracker2.domain.model.ScheduledIntakeDetails
import com.galeria.medtracker2.domain.repository.IntakesRepository
import com.galeria.medtracker2.domain.repository.MedicationsCourseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

data class AScheduleUiState(
    val todayIntakes: List<ScheduledIntakeDetails> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String = ""
)

@HiltViewModel
class AMainIntakesVM
@Inject
constructor(
    private val regimentsRepository: MedicationsCourseRepository,
    private val intakesRepository: IntakesRepository,
) : ViewModel() {

    val uiState: StateFlow<AScheduleUiState> =
        regimentsRepository
            .getFullSchedule()
            .distinctUntilChanged()
            .map { allRecords ->
                val now = LocalDate.now()
                AScheduleUiState(
                    //plannedIntakes = allRecords,
                    todayIntakes =
                        allRecords.filter {
                            DateTimeUtils.fromLongToLocalDate(it.scheduledTimestamp)==now
                        },
                    isLoading = false,
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = AScheduleUiState(isLoading = true),
            )

    fun checkIntake(status: Boolean, plannedIntakeId: UUID, timeStamp: Long) {
        viewModelScope.launch {
            try {
                val log =
                    IntakeLogDomain(
                        id = UUID.randomUUID(),
                        plannedIntakeId = plannedIntakeId,
                        actualTimestamp = Instant.ofEpochSecond(timeStamp),
                        isTaken = status,
                        notes = ""
                    )
                intakesRepository.addIntake(log)
            } catch (e: Exception) {
                Log.e("aMainIntakesVM", "Failed to mark intake", e)

            }
        }
    }

}