package com.galeria.medtracker2.feature.intakes.domain

import java.time.Instant
import java.util.UUID

data class IntakeLogDomain(
    val id: UUID,
    val plannedIntakeId: UUID,
    val actualTimestamp: Instant,
    val isTaken: Boolean,
    val notes: String = "",
)
