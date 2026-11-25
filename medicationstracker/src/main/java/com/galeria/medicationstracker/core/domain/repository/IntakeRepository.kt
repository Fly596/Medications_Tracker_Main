package com.galeria.medicationstracker.core.domain.repository

import com.galeria.medicationstracker.domain.medications.model.Intake

interface IntakeRepository {

    suspend fun addIntake(intake: Intake): Result<String>
}