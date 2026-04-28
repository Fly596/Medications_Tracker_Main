package com.galeria.medtracker2.feature.intakes.domain

import java.time.Instant
import java.util.UUID

data class IntakeDomain(
    val id: UUID,
    val medicationScheduleId: UUID,
    val actualIntakeDateTime: Instant,
    val notes: String = ""
)