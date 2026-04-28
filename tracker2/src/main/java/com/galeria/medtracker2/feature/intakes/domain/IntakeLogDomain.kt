package com.galeria.medtracker2.feature.intakes.domain

import java.time.Instant
import java.util.UUID

data class IntakeLogDomain(
    val id: UUID,
    val medicationScheduleId: UUID,
    val actualIntakeDateTime: Instant,
    val status: Boolean,
    val notes: String = ""
)