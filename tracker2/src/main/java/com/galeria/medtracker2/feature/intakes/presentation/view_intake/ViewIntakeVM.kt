package com.galeria.medtracker2.feature.intakes.presentation.view_intake

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galeria.medtracker2.domain.repository.IntakesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

data class ViewIntakeUiState(
    val intakeId: Long? = null,
    val intakeAmount: Double? = null,
    val intakeUnit: String? = null,
    val intakePrice: Long? = null,
    val intakeCurrency: String? = null,
    val intakeTimestamp: Instant? = null,
)

@HiltViewModel
class ViewIntakeVM @Inject constructor(
    private val intakeRepository: IntakesRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViewIntakeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // Получаем id приема.
        val intakeIdLong = savedStateHandle.get<Long>("intakeId")
        viewModelScope.launch {
            val intake =
                    if (intakeIdLong == null) null else intakeRepository.getIntakeById(intakeIdLong)

            if (intake != null) {
                _uiState.update {
                    it.copy(
                        intakeId = intakeIdLong,
                        intakeAmount = intake.dose.amount,
                        intakeUnit = intake.dose.unit.name,
                        intakePrice = intake.cost?.cents,
                        intakeCurrency = intake.cost?.currencyCode,
                        intakeTimestamp = intake.intakeDateTime
                    )
                }
            }

        }
    }
}