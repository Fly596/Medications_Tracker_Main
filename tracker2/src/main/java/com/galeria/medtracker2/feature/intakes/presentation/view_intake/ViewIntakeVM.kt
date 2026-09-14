package com.galeria.medtracker2.feature.intakes.presentation.view_intake

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.galeria.medtracker2.domain.repository.IntakesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class ViewIntakeUiState(
    val intakeId: Long? = null,
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
        if (intakeIdLong != null) {
            _uiState.update {
                it.copy(intakeId = intakeIdLong)
            }
        }
    }
}