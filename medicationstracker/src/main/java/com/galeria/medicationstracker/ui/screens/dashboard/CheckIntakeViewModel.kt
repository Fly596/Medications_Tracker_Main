package com.galeria.medicationstracker.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import com.galeria.medicationstracker.data.AuthRepository
import com.galeria.medicationstracker.data.IntakeStatus
import com.galeria.medicationstracker.data.NewIntakeRepository
import com.galeria.medicationstracker.data.NewMedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalTime
import javax.inject.Inject

data class IntakeUiState(
    val status: IntakeStatus = IntakeStatus.PENDING,
    val intakeTime: LocalTime = LocalTime.now(),
)

@HiltViewModel
class CheckIntakeViewModel
@Inject
constructor(
    private val intakeRepository: NewIntakeRepository,
    private val medicationRepository: NewMedicationRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {}
