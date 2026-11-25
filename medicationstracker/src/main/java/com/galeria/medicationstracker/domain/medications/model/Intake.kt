package com.galeria.medicationstracker.domain.medications.model

import java.time.Instant

data class Intake(
    val id: String,
    val medicationId: String,
    val presetTime: String? = null,
    val intakeTime: Instant = Instant.now(),
    val status: IntakeStatus = IntakeStatus.PENDING,
)